﻿using Assignment1;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace DA259E_Assignment1.Assignment1.Entities
{
    public class Villa : House
    {
        public Villa(int id, Categories category, string type, LegalForms legalForm, Address address, Image image, string imagename)
            : base(id, category, type, legalForm, address, image, imagename)
        { }
    }
}
