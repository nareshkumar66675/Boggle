using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text;
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
                text = text.Replace(stopWord.ToLower(), "");
            });

            return text;
        }
        
    }
}
