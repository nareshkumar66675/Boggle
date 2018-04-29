using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Crawler
{
    public class WebCall
    {
        public static string RetrieveHTML(string urlString)
        {
            try
            {
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(urlString);
                request.AllowAutoRedirect = true;
                request.Timeout = 10000;
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();

                
                if (response.StatusCode != HttpStatusCode.OK )
                    return null;
                //Console.WriteLine("Content length is {0}", response.ContentLength);
                //Console.WriteLine("Content type is {0}", response.ContentType);
                if (!response.ContentType.ToLower().Contains("html"))
                    return null;
                // Get the stream associated with the response.
                //Console.WriteLine(response.ContentType);
                Stream receiveStream = response.GetResponseStream();

                // Pipes the stream to a higher level stream reader with the required encoding format. 
                StreamReader readStream = new StreamReader(receiveStream, Encoding.UTF8);
                var result = readStream.ReadToEnd();
                //Console.WriteLine(stop.Elapsed);
                //Console.WriteLine("Response stream received.");
                //Console.WriteLine();
                response.Close();
                readStream.Close();

                if (string.IsNullOrWhiteSpace(result))
                    return null;

                return result;
            }
            catch (WebException wEx)
            {
                Console.WriteLine(urlString);
                Console.WriteLine(wEx);
                return null;
            }catch (Exception ex)
            {
                Console.WriteLine(urlString);
                Console.WriteLine(ex);
                return null;
            }
        }
    }
}
