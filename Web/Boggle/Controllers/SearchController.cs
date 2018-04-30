using Boggle.Models;
using Boggle.Services;
using Swashbuckle.Swagger.Annotations;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace Boggle.Controllers
{
    public class SearchController : ApiController
    {
        // GET api/values
        [HttpGet]
        [SwaggerOperation("Search")]
        public IEnumerable<SearchResult> Search(string query)
        {
            SearchService srchSrv = new SearchService();

            var result = srchSrv.Search(query);

            return result;
        }
    }
}
