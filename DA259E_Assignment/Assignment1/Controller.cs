using DA259E_Assignment1.Assignment1.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Drawing;

namespace Assignment1
{
    public class Controller
    {
        public string ChooseImage()
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Filter = "Image files (*.jpg, *.jpeg, *.jpe, *.jfif, *.png) | *.jpg; *.jpeg; *.jpe; *.jfif; *.png";
            if (dlg.ShowDialog() != DialogResult.OK)
                return null;
            return dlg.FileName;
        }

        public Estate GetEstate(int id, List<Estate> estates)
        {
            int i = 0;
            foreach (Estate estate in estates)
            {
                if (estate.ID == id) return estates[i];
                i++;
            }
            return null;
        }

        public Estate CreateEstate(int id, Categories category, string type, LegalForms legalForm, string street, string zipcode, string city, Countries country, Image image, string imagename)
        {
            switch (category)
            {
                case Categories.Commercial:
                    switch (type)
                    {
                        case "Shop":
                            return new Shop(id, category, type, legalForm, new Address(street, zipcode, city, country), image, imagename);
                        case "Warehouse":
                            return new Warehouse(id, category, type, legalForm, new Address(street, zipcode, city, country), image, imagename);
                        default:
                            return null;
                    }
                case Categories.Residential:
                    switch (type)
                    {
                        case "Apartment":
                            return new Apartment(id, category, type, legalForm, new Address(street, zipcode, city, country), image, imagename);
                        case "House":
                            return new House(id, category, type, legalForm, new Address(street, zipcode, city, country), image, imagename);
                        case "Villa":
                            return new Villa(id, category, type, legalForm, new Address(street, zipcode, city, country), image, imagename);
                        case "TownHouse":
                            return new TownHouse(id, category, type, legalForm, new Address(street, zipcode, city, country), image, imagename);
                        default:
                            return null;
                    }
                default:
                    return null;
            }
        }

        public List<string> SearchEstates(String searchTerm, List<Estate> estates)
        {
            if (searchTerm == "") return null;
            List<string> ids = new List<string>();
            foreach (Estate estate in estates)
            {
                string estateString = estate.ToSearchableString();
                if (estateString.Contains(searchTerm.Trim().ToLower()))
                    ids.Add(estate.ID.ToString());
            }
            return ids;
        }

        public List<string> SearchEstates(String[] searchTerms, List<Estate> estates)
        {
            if (searchTerms == null) return null;
            List<string> ids = new List<string>();
            foreach (Estate estate in estates)
            {
                string estateString = estate.ToSearchableString();
                if (ContainsWordArray(estateString, searchTerms))
                    ids.Add(estate.ID.ToString());
            }
            return ids;
        }

        public bool ContainsWordArray(string estateString, string[] searchTerms)
        {
            bool found = false;
            foreach (string searchTerm in searchTerms)
            {
                string test = searchTerm.Trim().ToLower();
                if (estateString.ToLower().Contains(test))
                {
                    found = true;
                    break;
                }
            }
            return found;
        }

    }
}
