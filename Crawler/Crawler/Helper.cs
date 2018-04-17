using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Crawler
{
    public class Helper
    {
        
        static Regex _LinkRegex = new Regex(@"(<a.*?>.*?</a>)", RegexOptions.Compiled| RegexOptions.Singleline);
        static Regex _hrefRegex = new Regex(@"href=\""(.*?)\""", RegexOptions.Compiled | RegexOptions.Singleline);

        static int fileNumber = 0;
        private static Object thisLock = new Object();

        public static List<Uri> GetAllValidHyperLinks(string file)
        {
            List<Uri> list = new List<Uri>();

            // 1.
            // Find all matches in file.
            MatchCollection m1 = _LinkRegex.Matches(file);// Regex.Matches(file, @"(<a.*?>.*?</a>)",
                //RegexOptions.Singleline);

            // 2.
            // Loop over each match.
            foreach (Match m in m1)
            {
                string value = m.Groups[1].Value;
                string link = string.Empty;

                // 3.
                // Get href attribute.
                Match m2 = _hrefRegex.Match(value); //Regex.Match(value, @"href=\""(.*?)\""",
                    //RegexOptions.Singleline);
                if (m2.Success)
                {
                    link = m2.Groups[1].Value;
                }

                //// 4.
                //// Remove inner tags from text.
                //string t = Regex.Replace(value, @"\s*<.*?>\s*", "",
                //    RegexOptions.Singleline);
                //i.Text = t;



                if (!string.IsNullOrWhiteSpace(link))
                {
                    Uri result;
                    if (Uri.TryCreate(link, UriKind.Absolute, out result))
                    {
                        if (result.Host == Config.Domain.Host)
                            list.Add(result);
                    }else if(Uri.TryCreate(Config.Domain, link,out result))
                    {
                        list.Add(result);
                    }
                }
            }
            return list;
        }

        public static void WriteHtmlToFile(string htmlText, URLData urlData)
        {
            string metaData = string.Format("/* URI:{0} ;:| Hit:{1} ;:| Hierarchy:{2} */\n",urlData.URL.AbsoluteUri,urlData.Hit,urlData.Hierarchy);

            string fileName =Path.GetFileName(urlData.URL.LocalPath).Trim(new char[] { '\\', '/' });
            string fullPath = Path.Combine(Config.HtmlFolder, Path.GetFileNameWithoutExtension(fileName)+".htmltxt");
            int count = 1;
            while(File.Exists(fullPath))
            {
                var fileNo = 0;
                lock(thisLock)
                {
                    fileNo = fileNumber;
                    fileNumber++;
                }
                fullPath = Path.Combine(Config.HtmlFolder, Path.GetFileNameWithoutExtension(fileName) + fileNo + ".htmltxt");
                count++;
            }

            File.WriteAllText(fullPath, metaData + htmlText);

        }
    }
}
