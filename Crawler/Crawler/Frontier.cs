using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Crawler
{
    public class URLData
    {
        public string URL { get; set; }

        public int Hit { get; set; } 

        public int Hierarchy { get; set; }

        public void IncreaseHit()
        {
            Hit++;
        }

        public void IncreaseHierarchy()
        {
            Hierarchy++;
        }
    }
    public static class Frontier
    {
        public static ConcurrentDictionary<string, URLData> CurrentQueue { get; set; }

        public static ConcurrentDictionary<string, URLData> CompletedQueue { get; set; }

        static Frontier()
        {
            CurrentQueue = new ConcurrentDictionary<string, URLData>(Environment.ProcessorCount * 2, 200);
            CompletedQueue = new ConcurrentDictionary<string, URLData>(Environment.ProcessorCount * 2, 10000);
        }
         
    }
}
