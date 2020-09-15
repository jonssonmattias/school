using Assignment1;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;

namespace DA259E_Assignment1.Assignment1.Entities
{
    abstract class CommercialBuilding : Estate
    {

        public CommercialBuilding(int id, Categories category, string type, LegalForms legalForm, Address address, Image image, string imagename)
            : base(id, category, type, legalForm, address, image, imagename)
        { }

        public override string[] Print()
        {
            return new[] { this.ID.ToString(), this.Category.ToString(), this.Type.ToString(), this.LegalForm.ToString(), this.Address.Street, this.Address.Zipcode, this.Address.City, this.Address.Country.ToString() };
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
