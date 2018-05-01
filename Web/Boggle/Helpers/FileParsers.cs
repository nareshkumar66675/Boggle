using Boggle.Models;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace Boggle.Helpers
{
    public static class FileParsers
    {
        public static CrawledData ParseCrawledData()
        {
            CrawledData crawledData = new CrawledData();
            string crawledDataPath = ConfigurationManager.AppSettings["CrawledData"];
            var fileLines = File.ReadAllLines(crawledDataPath);

            bool skipFirst = true;

            foreach (var line in fileLines)
            {
                if(skipFirst)
                {
                    skipFirst = false;
                    continue;
                }

                var values = line.Split(new string[] { "|:|" }, StringSplitOptions.None);

                URLData uRLData = new URLData(values[1].Trim('"'), values[2].Trim('"'), int.Parse(values[3]), int.Parse(values[4]), (Status)Enum.Parse(typeof(Status), values[5], true), int.Parse(values[0]));

                crawledData.URLs.Add(uRLData);
            }

            return crawledData;
        }

        public static SearchIndex ParseIndex()
        {
            SearchIndex searchIndex = new SearchIndex();
            
            var taskTerm = Task.Factory.StartNew(() => ParseTermIDF());
            var taskDoc = Task.Factory.StartNew(() => ParseDocumentData());

            taskTerm.Wait();
            taskDoc.Wait();
            searchIndex.TermIDF = taskTerm.Result;
            searchIndex.Documents = taskDoc.Result;
            return searchIndex; 
        }

        private static List<DocData> ParseDocumentData()
        {
            List<DocData> documents = new List<DocData>();

            string docVectorPath = ConfigurationManager.AppSettings["DocVectorPath"];
            string docLengthPath = ConfigurationManager.AppSettings["DocLengthPath"];

            using (StreamReader vectorRdr = new StreamReader(docVectorPath))
            {
                using (StreamReader lengthRdr = new StreamReader(docLengthPath))
                {
                    string vectorLine,lengthLine;
                    while ((vectorLine = vectorRdr.ReadLine()) != null)
                    {

                        DocData docData = new DocData();

                        var vectorValues = vectorLine.Split(':');


                        var task = Task.Factory.StartNew(()=> ParseDF(vectorValues[1]));

                        docData.FileNo = int.Parse(Path.GetFileNameWithoutExtension(vectorValues[0]).Split('_').Last());

                        lengthLine = lengthRdr.ReadLine();
                        var lengthValues = lengthLine.Split(':');

                        if(vectorValues[0].Trim() == lengthValues[0].Trim())
                        {
                            docData.Length = float.Parse(lengthValues[1].Trim());
                        }
                        else
                        {
                            throw new Exception("Vector and Length not matching");
                        }
                        task.Wait();
                        docData.DocVector = task.Result;

                        documents.Add(docData);
                    }
                }
            }

            return documents;
        }

        private static Dictionary<string, float> ParseDF(string line)
        {
            Dictionary<string, float> result = new Dictionary<string, float>();

            var pairs = line.Split(',');

            for (int i = 0; i < pairs.Length; i++)
            {
                var values = pairs[i].Split('=');
                if (i==0 || i== pairs.Length-1)
                {
                    result.Add(values[0].Replace("{", "").Trim(), float.Parse(values[1].Replace("}", "").Trim()));
                }
                else
                {
                    result.Add(values[0].Trim(), float.Parse(values[1].Trim()));
                }
            }

            return result;
        }
        private static Dictionary<string,float> ParseTermIDF()
        {
            string termIDFPath = ConfigurationManager.AppSettings["TermIDFPath"];
            Dictionary<string, float> termIDF = new Dictionary<string, float>();

            if(File.Exists(termIDFPath))
            {
                using (StreamReader reader = new StreamReader(termIDFPath))
                {
                    string line;
                    while ((line = reader.ReadLine()) != null)
                    {
                        var values = line.Split(':');
                        termIDF.Add(values[0].Trim(), float.Parse(values[1]));
                    }
                }
            }

            return termIDF;
        }
    }
}