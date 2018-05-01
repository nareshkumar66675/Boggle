using Boggle.Helpers;
using Boggle.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace Boggle.Services
{
    public class SearchService
    {
        public static CrawledData crawledData = new CrawledData();
        public static SearchIndex searchIndex = new SearchIndex();

        public static void Initialize()
        {
            var taskCrawlData = Task.Factory.StartNew(() => FileParsers.ParseCrawledData());
            var taskIndex = Task.Factory.StartNew(() => FileParsers.ParseIndex());

            taskCrawlData.Wait();
            taskIndex.Wait();

            crawledData = taskCrawlData.Result;
            searchIndex = taskIndex.Result;
        }

        public List<SearchResult> Search(string text)
        {
            var queryText = QueryParsers.ParseQuery(text).Split(null);

            var taskQry = Task.Factory.StartNew(() => GetQueryVector(queryText));
            var taskdoc = Task.Factory.StartNew(() => GetDocumentVector(queryText));
            taskQry.Wait();
            taskdoc.Wait();

            var qryVctr = taskQry.Result;

            var docVctr = taskdoc.Result;

            var rankedDocs = GetRankedDocuments(qryVctr, docVctr);

            return GetSearchResult(rankedDocs);

        }

        private List<SearchResult> GetSearchResult(Dictionary<int, float> rankedDocs)
        {
            List<SearchResult> searchRslt = new List<SearchResult>();
            
            var rankedResult = crawledData.URLs.Where(t => rankedDocs.ContainsKey(t.FileNo));

            foreach (var urlData in rankedResult)
            {
                SearchResult rslt = new SearchResult();

                rslt.Hit = urlData.Hit;
                rslt.Key = urlData.Key;
                rankedDocs.TryGetValue(urlData.FileNo, out float tempSimValue);
                rslt.SimValue = tempSimValue;
                rslt.URL = urlData.URL;

                searchRslt.Add(rslt);
            }
            var finalRslt = (from s in searchRslt
                     orderby s.SimValue descending, s.Hit descending
                     group s by s.Key into g
                     select g.First()).Take(2).ToList();

            List<Task<OpenGraph>> grpTasks = new List<Task<OpenGraph>>();
            foreach (var rslt in finalRslt)
            {
                var tsk = Task.Factory.StartNew(()  => rslt.MetaData= WebGraph.GetGraphDetails(rslt.URL));
                grpTasks.Add(tsk);
            }

            Task.WaitAll(grpTasks.ToArray());

            return finalRslt;
        }

        private Dictionary<string,float> GetQueryVector(string[] queryText)
        {
            Dictionary<string, float> qryVctr = new Dictionary<string, float>();

            foreach (var query in queryText)
            {
                searchIndex.TermIDF.TryGetValue(query, out float idfValue);
                qryVctr.Add(query, idfValue);
            }
            
            return qryVctr;
        }

        private List<DocData> GetDocumentVector(string[] queryText)
        {
            List<DocData> docVctr = new List<DocData>();

            foreach (var query in queryText)
            {
                docVctr.AddRange(searchIndex.Documents.Where(t => t.DocVector.ContainsKey(query.Trim())));
            }

            return docVctr.Distinct().ToList();
        }

        private Dictionary<int, float> GetRankedDocuments(Dictionary<string, float> qryVctr, List<DocData> docsVctr)
        {
            Dictionary<int, float> simValues = new Dictionary<int, float>();
            float qryLength = CalulateLength(qryVctr);
            foreach (var doc in docsVctr)
            {
                var sim = CalculateSimilarity(qryVctr, doc.DocVector, doc.Length, qryLength);

                simValues.Add(doc.FileNo, sim);
            }

            return simValues;
        }

        private float CalulateLength(Dictionary<string, float> Vctr)
        {
            float sum = 0;
            foreach (var item in Vctr)
            {
                sum += item.Value * item.Value;
            }

            return (float)Math.Sqrt(sum);
        }

        private float CalculateSimilarity(Dictionary<string, float> qryVctr, Dictionary<string, float> docVctr, float docLength,float qryLength)
        {
            float sum = 0;
            foreach (var term in qryVctr)
            {
                float docIDFValue = 0;
                docVctr.TryGetValue(term.Key, out docIDFValue);
                sum += term.Value * docIDFValue;
            }

            return (sum / (docLength * qryLength));
        }

    }
}