using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Assignment1
{
    abstract class Estate : IEstate
    {
        public abstract string[] Print();       
        public Adress Adress { get; set; }
        public int ID { get; set; }
        public string Category { get; set; }
        public string Type { get; set; }
        public string LegalForm { get; set; }
    }
}
