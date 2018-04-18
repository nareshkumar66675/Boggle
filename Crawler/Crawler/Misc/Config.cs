using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Crawler
{
    public static class Config
    {
        public static readonly int MaxHierarchyLevel = 5;
        public static readonly Uri Domain = new Uri("https://www.wikipedia.org/");
        public static readonly string DomainName = "en.wikipedia.org";

        public static readonly string HtmlFolder = @"C:\Users\Naresh\Desktop\Html";
    }
}
