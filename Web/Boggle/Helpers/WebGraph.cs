using Boggle.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Web;

namespace Boggle.Helpers
{
    public static class WebGraph
    {
        public static OpenGraph GetGraphDetails(string url)
        {
            OpenGraph graph = new OpenGraph();
            var urlEncoded = Uri.EscapeDataString(url);
            var requestUrl = "https://maxmorgandesign.com/url-details/?url=" + urlEncoded + "&callback=angular.callbacks._1";
           // var requestUrl = "https://opengraph.io/api/1.1/site/" + urlEncoded;

            // Make sure to get your API key!  No need for a CC
            //requestUrl += "?app_id=5ae7ef799b03547407c64913";

            var request = WebRequest.Create(requestUrl);
            request.ContentType = "application/json;";

            string text;

            var response = (HttpWebResponse)request.GetResponse();

            using (var sr = new StreamReader(response.GetResponseStream()))
            {
                text = sr.ReadToEnd();
                text = text.Remove(text.Length -2).Replace("angular.callbacks._1(", "");
                dynamic x = JsonConvert.DeserializeObject(text);

                graph.Description = x?.Description?.Value;
                graph.Title= x?.Title?.Value;
                graph.SiteName = x?.Site?.Value;

            }

            return graph;
        }
    }
}