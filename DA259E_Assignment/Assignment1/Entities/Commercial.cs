/*using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assignment1
{
    class Commercial : Estate
    {
        private string[] typeRes = { "Houses", "Villas", "Apartments", "Townhouses" };

        public override string[] Print()
        {
            return new[] { this.ID.ToString(), this.Category, this.Type, this.LegalForm, this.Adress.Street, this.Adress.Zipcode, this.Adress.City, this.Adress.Country.ToString() };
        }

        public override string ToSearchableString()
        {
            string str =
               $"{this.ID} " +
               $"{this.Category.ToLower()} " +
               $"{this.Type.ToLower()} " +
               $"{this.LegalForm.ToLower()} " +
               $"{this.Adress.Street.ToLower()} " +
               $"{this.Adress.Zipcode} " +
               $"{this.Adress.City.ToLower()} " +
               $"{this.Adress.Country.ToString().ToLower()}";

            return str;
        }
    }
}
*/