using DocumentParser;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Boggle.Helpers
{
    public static class QueryParsers
    {
        public static string ParseQuery(string query)
        {
            string result = string.Empty;

            Parser parse = new Parser();
            return parse.Parse(query);
        }
    }
}