using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Boggle.Models
{

    public class SearchResult
    {
        public string Key { get; set; }

        public string URL { get; set; }

        public float SimValue { get; set; }

        public float Hit { get; set; }
    }
}