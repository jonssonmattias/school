using Assignment1;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DA259E_Assignment1.Assignment1.Entities
{
    class Ownership : LegalForm
    {
        public float Price { get; set; }

        public float Fee { get; set; }

        public override string ToString()
        {
            return LegalForms.Ownership.ToString();
        }
    }
}
