using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Crawler
{
    public static class Config
    {
        public static readonly int MaxHierarchyLevel = 6;
        public static readonly int MaxDocumentCount = 3000;
        public static readonly int MaxThreads = 5;
        public static readonly Uri Domain = new Uri("https://www.imdb.com/"); //Uri("https://www.wikipedia.org/");
        public static readonly string DomainMatch =  "www.imdb.com/title"; //"dictionary.com"; //"en.wikipedia.org";
        //www.dictionary.com
        public static readonly string HtmlFolder = @"C:\Users\Naresh\Desktop\Html";
    }
}
