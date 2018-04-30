using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace DocumentParser
{
    public static class StopWords
    {
        public static List<string> stopWords = new List<string>();

        static StopWords()
        {
            stopWords = ConfigurationManager.AppSettings["StopWords"].Split(',').ToList();
        }

        public static string ApplyStopWords(string text)
        {
            stopWords.ForEach(stopWord =>
            {
                text = Regex.Replace(text, @"\b"+ stopWord.ToLower() + @"\b", "");
            });

            return text;
        }
        
    }
}
