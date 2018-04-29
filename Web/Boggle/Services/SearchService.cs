using Boggle.Helpers;
using Boggle.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Boggle.Services
{
    public class SearchService
    {
        public static CrawledData crawledData = new CrawledData();

        public static void Initialize()
        {
            crawledData = FileParsers.ParseCrawledData();
        }

    }
}