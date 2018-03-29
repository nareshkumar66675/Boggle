using DocumentParser;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Crawler
{
    class Program
    {
        static void Main(string[] args)
        {
            Parser parser = new Parser();

            string text = string.Empty;//= File.ReadAllText(@"C:\Users\Naresh\Downloads\docsnew\docsnew\Atlanta_Falcons_seasons.htm");

            Directory.EnumerateFiles(@"C:\Users\Naresh\Downloads\docsnew\docsnew\").ToList().ForEach(t =>
            {
                text = File.ReadAllText(t);
                var result = parser.Parse(text);

                File.WriteAllText(Path.Combine(@"C:\Users\Naresh\Desktop\HTML", Path.GetFileName(t)), result);
            });


            Console.WriteLine("");

        }
    }
}
