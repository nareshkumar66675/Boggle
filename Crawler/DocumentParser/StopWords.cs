using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace DocumentParser
{
    public static class StopWords
    {
        public static string ApplyStopWords(string text)
        {
            string stopWordsString = ConfigurationManager.AppSettings["StopWords"];
            var stopWords = stopWordsString.Split(',').ToList();
            stopWords.ForEach(stopWord =>
            {
                text = Regex.Replace(text, @"\b"+ stopWord.ToLower() + @"\b", "");
            });

            return text;
        }
        
    }
}
