using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Boggle.Models
{
    public class IDFIndex
    {
        public List<DocData> Documents { get; set; }

        public Dictionary<string,float> IDF { get; set; }
    }

    public class DocData
    {
        public int FileNo { get; set; }

        public float Length { get; set; }

        public Dictionary<string,string> Terms { get; set; }
    }
}