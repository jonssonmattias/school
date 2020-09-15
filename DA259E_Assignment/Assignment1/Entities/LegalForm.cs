using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DA259E_Assignment1.Assignment1.Entities
{
    public abstract class LegalForm
    {
        /*public LegalForm(LegalForms legalForms)
        {
            switch (legalForms)
            {
                case LegalForms.Ownership:
                    new Ownership();
            }
        }*/

        public abstract override string ToString();
    }
}
