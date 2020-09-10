using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assignment1
{
    class Residential : Estate
    {
        private string[] typeRes = { "Houses", "Villas", "Apartments", "Townhouses" };

        public override string[] Print()
        {
            return new[] { this.ID.ToString(), this.Category, this.Type, this.LegalForm, this.Adress.Street, this.Adress.Zipcode, this.Adress.City, this.Adress.Country.ToString() };
        }
    }
}
