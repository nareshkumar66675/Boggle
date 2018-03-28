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

            string text = File.ReadAllText(@"C:\Users\Naresh\Downloads\docsnew\docsnew\Atlanta_Falcons_seasons.htm");

            var result = parser.Parse(text);

            File.WriteAllText(@"C: \Users\Naresh\Desktop\htm.txt", result);

            Console.WriteLine(result);

        }
    }
}
