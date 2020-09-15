/*using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assignment1
{
    class Residential : Estate
    {
        private string[] typeRes = { "House", "Villa", "Apartment", "Townhouse" };

        public override string[] Print()
        {
            return new[] { this.ID.ToString(), this.Category.ToString(), this.Type, this.LegalForm.ToString(), this.Address.Street, this.Address.Zipcode, this.Address.City, this.Address.Country.ToString() };
        }

        public override string ToSearchableString()
        {
            string str = 
                $"{this.ID} " +
                $"{this.Category.ToString().ToLower()} " +
                $"{this.Type.ToLower()} " +
                $"{this.LegalForm.ToString().ToLower()} " +
                $"{this.Address.Street.ToLower()} " +
                $"{this.Address.Zipcode} " +
                $"{this.Address.City.ToLower()} " +
                $"{this.Address.Country.ToString().ToLower()}";

            return str;       
        }
    }
}
*/