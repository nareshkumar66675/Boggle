using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DocumentParser
{
    public class Parser
    {
        public string Parse(string text)
        {
            string result = string.Empty;
            result =HTMLParser.RemoveHTMLTags(text);
            result = PreProcess(result);
            result = StopWords.ApplyStopWords(result);

            PorterStemmer porter = new PorterStemmer();
            result=porter.StemText(result);

            return result;
        }

        private string PreProcess(string text)
        {
            string result = string.Empty;
            //Convert To Lower
            result = text.ToLower();
            //Remove Punctuations
            result = new string(result.Where(c => (!char.IsPunctuation(c)||!char.IsNumber(c))).ToArray());
            return result;
        }
    }
}
