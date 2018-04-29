using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Boggle.Models
{
    public class SearchIndex
    {
        public List<DocData> Documents { get; set; }

        public Dictionary<string,float> TermIDF { get; set; }
    }

    public class DocData
    {
        public int FileNo { get; set; }

        public float Length { get; set; }

        public Dictionary<string,float> DocVector { get; set; }

        public DocData()
        {
            DocVector = new Dictionary<string, float>();
        }
    }
}