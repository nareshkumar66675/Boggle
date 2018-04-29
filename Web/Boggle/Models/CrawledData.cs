using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Boggle.Models
{
    public class CrawledData
    {
        public List<URLData> URLs { get; set; }

        public CrawledData()
        {
            URLs = new List<URLData>();
        }
    }
    public enum Status
    {
        Success,
        Failed,
        InQueue
    }
    public class URLData
    {
        public string Key { get; set; }

        public string URL { get; set; }

        public int Hit { get; set; }

        public int Hierarchy { get; set; }

        public Status Status { get; set; }

        public int FileNo { get; set; }

        public URLData()
        {
            Hierarchy = 0;
            Hit = 0;
            Status = Status.InQueue;
            FileNo = 0;
        }
        public URLData(string key, string url, int hit, int hierarchy, Status status, int fileNo)
        {
            Key = key;
            URL = url;
            Hierarchy = hierarchy;
            Hit = hit;
            Status = status;
            FileNo = fileNo;
        }

    }
}