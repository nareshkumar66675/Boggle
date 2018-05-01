using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Boggle.Models
{
    public class OpenGraph
    {
        public string Title { get; set; }

        public string Description { get; set; }

        public string SiteName { get; set; }
    }

    public class SearchResult
    {
        public string Key { get; set; }

        public string URL { get; set; }

        public float SimValue { get; set; }

        public float Hit { get; set; }

        public OpenGraph MetaData { get; set; }
    }
}