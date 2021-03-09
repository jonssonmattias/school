using DA259E_Assignment1.Assignment1.Entities;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Assignment1
{
    public abstract class Estate : IEstate
    {
        protected Estate(int id, Categories category, string type, LegalForms legalForm, Address address, Image image, string imagename)
        {
            ID = id;
            Category = category;
            Type = type;
            LegalForm = legalForm;
            Address = address;
            Image = image;
            ImageFilename = imagename;
        }

        public abstract string[] Print();
        public abstract string ToSearchableString();
        public Address Address { get; set; }
        public int ID { get; set; }
        public Categories Category { get; set; }
        public string Type { get; set; }
        public LegalForms LegalForm { get; set; }
        public Image Image { get; set; }
        public string ImageFilename { get; set; }
    }
}

