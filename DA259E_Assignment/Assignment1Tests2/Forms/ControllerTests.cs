using Microsoft.VisualStudio.TestTools.UnitTesting;
using Assignment1;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;
using System.IO;

namespace Assignment1.Tests
{
    [TestClass()]
    public class ControllerTest
    {
        private Controller controller = new Assignment1.Controller();
        private Estate estate;
        private Image img;
        private List<Estate> estates = new List<Estate>();
        private int id = 0;

        public ControllerTest()
        {
            string imgName = "..\\..\\Images\\IMG_001.jpg";
            img = Image.FromFile(imgName);
            estate = controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 0), LegalForms.Rental, "Rådmansgatan 1A", "21146", "Malmö", Countries.Sweden, img, imgName);

            estates.Add(controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 2), LegalForms.Ownership, "Palmstreet 50C", "789345", "Kabul", Countries.Afghanistan, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Commercial, Enum.GetName(typeof(ComTypes), 0), LegalForms.Rental, "Torggatan 14", "52231", "Tidaholm", Countries.Sweden, img, ""));
            estates.Add(controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 3), LegalForms.Ownership, "22 Park Lane", "123456", "London", Countries.United_Kingdom, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 0), LegalForms.Rental, "Södra Förstadsgatan 33", "24614", "Malmö", Countries.Sweden, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Commercial, Enum.GetName(typeof(ComTypes), 0), LegalForms.Rental, "Avenyn 99", "51000", "Göteborg", Countries.Sweden, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Commercial, Enum.GetName(typeof(ComTypes), 1), LegalForms.Rental, "Knivgatan 10", "21412", "Malmö", Countries.Sweden, img, imgName));
        }

        [TestMethod()]
        public void IDTest()
        {
            Assert.AreEqual(0, estate.ID);
        }

        [TestMethod()]
        public void CategoryTest()
        {
            Assert.AreEqual("Residential", estate.Category.ToString());
        }

        [TestMethod()]
        public void TypeTest()
        {
            Assert.AreEqual("Apartment", estate.Type);
        }

        [TestMethod()]
        public void LegalFormTest()
        {
            Assert.AreEqual("Rental", estate.LegalForm.ToString());
        }

        [TestMethod()]
        public void StreetTest()
        {
            Assert.AreEqual("Rådmansgatan 1A", estate.Address.Street);
        }

        [TestMethod()]
        public void ZipCodeTest()
        {
            Assert.AreEqual("21146", estate.Address.Zipcode);
        }

        [TestMethod()]
        public void CityTest()
        {
            Assert.AreEqual("Malmö", estate.Address.City);
        }

        [TestMethod()]
        public void CountryTest()
        {
            Assert.AreEqual("Sweden", estate.Address.Country.ToString());
        }

        [TestMethod()]
        public void ImageTest()
        {
            Console.WriteLine(img);
            Assert.AreEqual(img, estate.Image);
        }

        [TestMethod()]
        public void ImageFilenameTest()
        {
            Assert.AreEqual("..\\..\\Images\\IMG_001.jpg", estate.ImageFilename);
        }


        [TestMethod()]
        public void SearchEstatesSingleTermTest()
        {
           string id = controller.SearchEstates("Torggatan", estates)[0];

            Assert.AreEqual("2", id);
        }

        [TestMethod()]
        public void SearchEstatesMultipleTermsTest()
        {
            string id = controller.SearchEstates(new string[] { "Torggatan" }, estates)[0];

            Assert.AreEqual("2", id);
        }

        [TestMethod()]
        public void ContainsWordArrayTest()
        {
            string[] array = new string[] { "A" };
            bool contains = controller.ContainsWordArray("ABCDEFG", array);

            Assert.AreEqual(true, contains);
        }

        [TestMethod()]
        public void GetImageTest()
        {
            string img = controller.ChooseImage();            
            Assert.AreEqual(Path.GetFullPath("..\\..\\Images\\IMG_001.jpg"), img);
        }
    }
}