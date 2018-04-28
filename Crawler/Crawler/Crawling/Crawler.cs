using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Crawler
{
    public class Crawler
    {
        
        public void Crawl(int MaxThread)
        {
            List<Task> listOfTasks = new List<Task>();
            int taskCount = 0;

            while(taskCount<MaxThread)
            {
                Spyder spyd = new Spyder();
                var tempTask = new Task(spyd.Start);
                tempTask.Start();
                listOfTasks.Add(tempTask);
                taskCount++;
            }
            Task.WaitAll(listOfTasks.ToArray());

            Helper.WriteCompletedQueue();
        }
    }
}
