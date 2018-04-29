using Boggle.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace Boggle.Helpers
{
    public static class FileParsers
    {
        public static CrawledData ParseCrawledData()
        {
            CrawledData crawledData = new CrawledData();
            string crawledDataPath = @"C:\Users\Naresh\Desktop\New folder\CompletedCrawlDetails04292018_160821.txt";
            var fileLines = File.ReadAllLines(crawledDataPath);

            bool skipFirst = true;

            foreach (var line in fileLines)
            {
                if(skipFirst)
                {
                    skipFirst = false;
                    continue;
                }

                var values = line.Split(new string[] { "|:|" }, StringSplitOptions.None);

                URLData uRLData = new URLData(values[1], values[2], int.Parse(values[3]), int.Parse(values[4]), (Status)Enum.Parse(typeof(Status), values[5], true), int.Parse(values[0]));

                crawledData.URLs.Add(uRLData);
            }

            return crawledData;
        }
    }
}