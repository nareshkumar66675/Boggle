using DocumentParser;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Crawler
{
    public class Spyders
    {
        public void Crawl()
        {
            string key = string.Empty;
            while((key=GetNextUrl())!=null)
            {
                if(Frontier.CurrentQueue.TryGetValue(key,out URLData urlData))
                {
                    if(Frontier.CompletedQueue.ContainsKey(key))//If its already completed
                    {
                        Frontier.CurrentQueue.TryRemove(key, out URLData removed); //Remove URL if exists in Complete
                        continue;
                    }
                    else
                    {
                        var htmlText = WebCall.RetrieveHTML(key);
                        if(!string.IsNullOrEmpty(htmlText)) // If No Html is returned
                        {
                            if (urlData.Hierarchy <= Config.MaxHierarchyLevel)
                            {
                                var allLinks = Helper.GetAllValidHyperLinks(htmlText);
                                AddAllHyperLinks(allLinks, urlData.Hierarchy);
                            }
                            Parser parser = new Parser();
                            var parseHTML = parser.Parse(htmlText);

                            Helper.WriteHtmlToFile(parseHTML, urlData);
                        }
                        Console.WriteLine("/* URI:{0} ;:| Hit:{1} ;:| Hierarchy:{2} */\n", urlData.URL.AbsoluteUri, urlData.Hit, urlData.Hierarchy);
                        int Iteration = 0;
                        while(!Frontier.CompletedQueue.TryAdd(urlData.URL.GetLeftPart(UriPartial.Path), urlData)) // Add to completed Queue
                        {
                            Thread.Sleep(1000);
                            if (Iteration > 100)
                                break;
                            Iteration++;
                        }

                    }
                }else
                {
                    continue;//Skip current url and move forward
                }
            }
        }

        private void AddAllHyperLinks(List<Uri> URIList,int parentHierarchy)
        {
            foreach (var uriItem in URIList)
            {
                if(Frontier.CompletedQueue.ContainsKey(uriItem.GetLeftPart(UriPartial.Path)))//Already Crawled
                {
                    if (Frontier.CompletedQueue.TryGetValue(uriItem.GetLeftPart(UriPartial.Path), out URLData completed))
                    {
                        completed.IncreaseHit();//Increase the Hit in complete queue
                    }
                }
                else if(Frontier.CurrentQueue.ContainsKey(uriItem.GetLeftPart(UriPartial.Path)))//Already in queue
                {
                    if (Frontier.CurrentQueue.TryGetValue(uriItem.GetLeftPart(UriPartial.Path), out URLData notCompleted))
                    {
                        notCompleted.IncreaseHit();//Increase the Hit in current queue
                    }
                }
                else//Not in Current Queue
                {
                    URLData tempURL = new URLData();
                    tempURL.URL = uriItem;
                    tempURL.Hierarchy = parentHierarchy + 1;
                    Frontier.CurrentQueue.AddOrUpdate(uriItem.GetLeftPart(UriPartial.Path), tempURL, (k, v) => { v.IncreaseHit(); return v; });
                }

            }
        }

        private string GetNextUrl()
        {
            int count = 0;
            while(count<100)
            {
                if (Frontier.CurrentQueue.IsEmpty)
                {
                    Thread.Sleep(5000);
                }
                else
                {
                    break;
                }
            }
            if (count > 99)
                return null;
            else
                return Frontier.CurrentQueue.OrderByDescending(t => t.Value.Hit).FirstOrDefault().Key;
        }
    }
}
