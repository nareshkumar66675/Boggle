using DocumentParser;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Crawler
{
    class Program
    {
        static void Main(string[] args)
        {
            ////var a= Environment.ProcessorCount;
            //RetrieveHTML();
            //Parser parser = new Parser();

            //string text = string.Empty;//= File.ReadAllText(@"C:\Users\Naresh\Downloads\docsnew\docsnew\Atlanta_Falcons_seasons.htm");

            //Directory.EnumerateFiles(@"C:\Users\Naresh\Downloads\docsnew\docsnew\").ToList().ForEach(t =>
            //{
            //    text = File.ReadAllText(t);
            //    var result = parser.Parse(text);

            //    File.WriteAllText(Path.Combine(@"C:\Users\Naresh\Desktop\HTML", Path.GetFileName(t)), result);
            //});


            //Console.WriteLine("");

            Crawler crawl = new Crawler();
            string startUrl = "https://www.imdb.com/chart/top";
            //string startUrl = "https://en.wikipedia.org/wiki/India";
            URLData urlData = new URLData();
            urlData.URL = new Uri(startUrl);
            //Frontier.CurrentQueue.TryAdd(Config.Domain.GetLeftPart(UriPartial.Path), urlData);
            
            Frontier.CurrentQueue.TryAdd(startUrl, urlData);
            crawl.Crawl(Config.MaxThreads);

        }
        public static void RetrieveHTML()
        {
            var stop =Stopwatch.StartNew();
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://www.youtube.com");
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();

            Console.WriteLine("Content length is {0}", response.ContentLength);
            Console.WriteLine("Content type is {0}", response.ContentType);

            // Get the stream associated with the response.
            Stream receiveStream = response.GetResponseStream();

            // Pipes the stream to a higher level stream reader with the required encoding format. 
            StreamReader readStream = new StreamReader(receiveStream, Encoding.UTF8);
            var temp = readStream.ReadToEnd();
            Console.WriteLine(stop.Elapsed);
            Console.WriteLine("Response stream received.");
            Console.WriteLine();
            response.Close();
            readStream.Close();
        }
    }
}
