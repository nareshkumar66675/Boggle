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
                        if(urlData.Hierarchy<=Config.MaxHierarchyLevel)
                        {
                            var allLinks = Helper.GetAllValidHyperLinks(htmlText);
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
                if(Frontier.CompletedQueue.ContainsKey(uriItem.AbsoluteUri))//Already Crawled
                {
                    if (Frontier.CompletedQueue.TryGetValue(uriItem.AbsoluteUri, out URLData completed))
                    {
                        completed.IncreaseHit();//Increase the Hit in complete queue
                    }
                }
                else if(Frontier.CurrentQueue.ContainsKey(uriItem.AbsoluteUri))//Already in queue
                {
                    if (Frontier.CurrentQueue.TryGetValue(uriItem.AbsoluteUri, out URLData notCompleted))
                    {
                        notCompleted.IncreaseHit();//Increase the Hit in current queue
                    }
                }
                else
                {
                    URLData tempURL = new URLData();
                    tempURL.URL = uriItem;
                    tempURL.Hierarchy = parentHierarchy + 1;
                    Frontier.CurrentQueue.TryAdd(uriItem.AbsoluteUri, tempURL);
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
            }
            if (count > 99)
                return null;
            else
                return Frontier.CurrentQueue.OrderBy(t => t.Value.Hit).FirstOrDefault().Key;
        }
    }
}
