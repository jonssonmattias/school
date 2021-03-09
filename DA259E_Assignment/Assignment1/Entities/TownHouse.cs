using Assignment1;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;

namespace DA259E_Assignment1.Assignment1.Entities
{
    public class TownHouse : House
    {
        public TownHouse(int id, Categories category, string type, LegalForms legalForm, Address address, Image image, string imagename)
            : base(id, category, type, legalForm, address, image, imagename)
        { }
    }
}
