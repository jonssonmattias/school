using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Windows.Forms;

namespace Assignment1
{
    public partial class Form1 : Form
    {
        private List<Estate> estates = new List<Estate>();
        private int id = 0, currEditEstateID;
        private ImageList imgs = new ImageList();
        private Controller controller;

        public Form1()
        {
            InitializeComponent();
            SetListViewColumns();
            PopulateCombobox(false, -1);
            imgs.ImageSize = new Size(100, 100);
            controller = new Controller();

        // TEST DATA        
            string imgName = "..\\..\\Images\\IMG_001.jpg";
            Image img = Image.FromFile(imgName);

            estates.Add(controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 0), LegalForms.Rental, "Rådmansgatan 1A", "21146", "Malmö", Countries.Sweden, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 2), LegalForms.Ownership, "Palmstreet 50C", "789345", "Kabul", Countries.Afghanistan, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Commercial, Enum.GetName(typeof(ComTypes), 0), LegalForms.Rental, "Torggatan 14", "52231", "Tidaholm", Countries.Sweden, img, ""));
            estates.Add(controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 3), LegalForms.Ownership, "22 Park Lane", "123456", "London", Countries.United_Kingdom, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Residential, Enum.GetName(typeof(ResTypes), 0), LegalForms.Rental, "Södra Förstadsgatan 33", "24614", "Malmö", Countries.Sweden, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Commercial, Enum.GetName(typeof(ComTypes), 0), LegalForms.Rental, "Avenyn 99", "51000", "Göteborg", Countries.Sweden, img, imgName));
            estates.Add(controller.CreateEstate(id++, Categories.Commercial, Enum.GetName(typeof(ComTypes), 1), LegalForms.Rental, "Knivgatan 10", "21412", "Malmö", Countries.Sweden, img, imgName));
            UpdateListView();
            // END TEST

        }

        private void SetListViewColumns()
        {
            listView1.View = View.Details;
            listView1.Columns.Add("Image", 100, HorizontalAlignment.Left);
            listView1.Columns.Add("ID", 50, HorizontalAlignment.Left);
            listView1.Columns.Add("Category", 50, HorizontalAlignment.Left);
            listView1.Columns.Add("Type", 50, HorizontalAlignment.Left);
            listView1.Columns.Add("Legal Form", 50, HorizontalAlignment.Left);
            listView1.Columns.Add("Street", 50, HorizontalAlignment.Left);
            listView1.Columns.Add("Zip Code", 50, HorizontalAlignment.Left);
            listView1.Columns.Add("City", 50, HorizontalAlignment.Left);
            listView1.Columns.Add("Country", 50, HorizontalAlignment.Left);
        }

        private void PopulateCombobox(bool edit, int cat)
        {
            PopulateCategory();
            PopulateLegalForm();
            PopulateCountries();
            if (edit) PopulateType(cat);
        }

        private void PopulateCategory()
        {
            cbCategory.DataSource = Enum.GetNames(typeof(Categories));
            cbCategoryEdit.DataSource = Enum.GetNames(typeof(Categories));
        }

        private void PopulateType(int cat)
        {
            if (cat == (int)Categories.Residential)
            {
                cbType.DataSource = Enum.GetNames(typeof(ResTypes));
                cbTypeEdit.DataSource = Enum.GetNames(typeof(ResTypes));
            }
            else if (cat == 1)
            {
                cbType.DataSource = Enum.GetNames(typeof(ComTypes));
                cbTypeEdit.DataSource = Enum.GetNames(typeof(ComTypes));
            }
        }

        private void PopulateLegalForm()
        {
            cbLegalForm.DataSource = Enum.GetNames(typeof(LegalForms));
            cbLegalFormEdit.DataSource = Enum.GetNames(typeof(LegalForms));
        }

        private void PopulateCountries()
        {
            cbCountry.DataSource = Enum.GetNames(typeof(Countries));
            cbCountryEdit.DataSource = Enum.GetNames(typeof(Countries));
        }

        private void ClearInput()
        {
            cbCategory.SelectedItem = null;
            cbType.SelectedItem = null;
            cbLegalForm.SelectedItem = null;
            tbxCity.Text = "";
            tbxStreet.Text = "";
            tbxZipCode.Text = "";
            cbCountry.SelectedItem = null;
            lblChosenImage.Text = "";

            cbCategoryEdit.SelectedItem = null;
            cbTypeEdit.SelectedItem = null;
            cbLegalFormEdit.SelectedItem = null;
            tbxCityEdit.Text = "";
            tbxStreetEdit.Text = "";
            tbxZipCodeEdit.Text = "";
            cbCountryEdit.SelectedItem = null;
        }

        private void AddToList(Estate estate)
        {            
            estates.Add(estate);
            listView1.View = View.Details;                      
            ListViewItem item = new ListViewItem(" ", 0);
            foreach (String s in estate.Print())
                item.SubItems.Add(s);
            
            imgs.Images.Add(estate.Image);
            listView1.SmallImageList = imgs;

            item.ImageIndex = (estates.Count - 1);
            listView1.Items.Add(item);                     
        }

        private void UpdateListView()
        {
            listView1.Items.Clear();
            RemoveImages();
            listView1.View = View.Details;
            int i = 0;
            foreach (Estate estate in estates)
            {
                listView1.View = View.Details;
                ListViewItem item = new ListViewItem(" ", 0);
                foreach (String s in estate.Print())
                    item.SubItems.Add(s);

                imgs.Images.Add(estate.Image);
                listView1.SmallImageList = imgs;

                item.ImageIndex = i++;
                listView1.Items.Add(item);
            }
        }

        private void RemoveImages()
        {
            int count = imgs.Images.Count;
            for (int i = count-1; i >= 0; i--) imgs.Images.RemoveAt(i);
        }                     

        private void SelectListViewItem(string ID)
        {
            foreach (ListViewItem item in listView1.Items)
                if (ID == item.SubItems[1].Text)
                    item.Selected = true;
        }

        private void ClearSelectedListViewItems()
        {
            if (listView1.SelectedItems.Count != 0)
                foreach (ListViewItem item in listView1.SelectedItems)
                    item.Selected = false;
        }

        private void BtnCreate_Click(object sender, EventArgs e)
        {
            Categories category = (Categories)cbCategory.SelectedIndex;
            string type = cbType.SelectedItem.ToString();
            LegalForms legalForm = (LegalForms)cbLegalForm.SelectedIndex;
            string street = tbxStreet.Text.Trim();
            string zipcode = tbxZipCode.Text.Trim();
            string city = tbxCity.Text.Trim();
            Countries country = (Countries)cbCountry.SelectedIndex;
            Image image = Image.FromFile(lblChosenImage.Text);
            string imagename = lblChosenImage.Text;

            Estate estate = controller.CreateEstate(id++, category, type, legalForm, street, zipcode, city, country, image, imagename);
            AddToList(estate);
            ClearInput();

        }

        private void Category_SelectedIndexChanged(object sender, EventArgs e)
        {
            PopulateType(((ComboBox)sender).SelectedIndex);
        }

        private void btnEdit_Click(object sender, EventArgs e)
        {
            Estate selectedEstate = estates[listView1.SelectedIndices[0]];

            gbxCreate.Enabled = false;
            gbxEdit.Enabled = true;

            int categoryIndex = (int)selectedEstate.Category;
            PopulateType(categoryIndex);

            currEditEstateID = selectedEstate.ID;
            cbCategoryEdit.SelectedIndex = categoryIndex;

            cbTypeEdit.SelectedIndex = categoryIndex == 0 ?
                Array.IndexOf((Array)cbTypeEdit.DataSource, selectedEstate.Type)
                : Array.IndexOf((Array)cbTypeEdit.DataSource, selectedEstate.Type);

            cbLegalFormEdit.SelectedIndex = (int)selectedEstate.LegalForm;
            tbxStreetEdit.Text = selectedEstate.Address.Street;
            tbxZipCodeEdit.Text = selectedEstate.Address.Zipcode;
            tbxCityEdit.Text = selectedEstate.Address.City;
            cbCountryEdit.SelectedIndex = (int)selectedEstate.Address.Country;
            lblChosenImageEdit.Text = selectedEstate.ImageFilename;
        }

        private void btnRemove_Click(object sender, EventArgs e)
        {
            estates.Remove(estates[listView1.SelectedIndices[0]]);
            listView1.Items.RemoveAt(listView1.SelectedIndices[0]);
        }

        private void Image_Click(object sender, EventArgs e)
        {
            if(sender.Equals(btnImage)) lblChosenImage.Text = controller.ChooseImage();
            else lblChosenImageEdit.Text = controller.ChooseImage();
        }

        private void btnSearch_Click(object sender, EventArgs e)
        {
            ClearSelectedListViewItems();
            if (tbxSearch.Text.Contains(','))
                foreach(string id in controller.SearchEstates(tbxSearch.Text.Split(','), estates))
                {
                    SelectListViewItem(id);
                }                               
            else
                foreach(string id in controller.SearchEstates(tbxSearch.Text, estates))
                {
                    SelectListViewItem(id);
                }                    
        }

        private void btnConfirmEdit_Click(object sender, EventArgs e)
        {
            Estate selectedEstate = controller.GetEstate(currEditEstateID, estates);
            selectedEstate.Category = (Categories)cbCategoryEdit.SelectedIndex;
            selectedEstate.Type = cbTypeEdit.SelectedItem.ToString();
            selectedEstate.LegalForm = (LegalForms)cbLegalFormEdit.SelectedIndex;
            selectedEstate.Address.Street = tbxStreetEdit.Text;
            selectedEstate.Address.Zipcode = tbxZipCodeEdit.Text;
            selectedEstate.Address.City = tbxCityEdit.Text;
            selectedEstate.Address.Country = (Countries)cbCountryEdit.SelectedIndex;
            selectedEstate.Image = Image.FromFile(lblChosenImageEdit.Text);
            selectedEstate.ImageFilename = lblChosenImageEdit.Text;

            UpdateListView();
            ClearInput();

            gbxCreate.Enabled = true;
            gbxEdit.Enabled = false;
        }
    }
}
