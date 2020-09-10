using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Assignment1
{
    public partial class Form1 : Form
    {
        private string[] categories = { "Residential", "Commercial" };
        private string[] typeRes = { "Houses", "Villas", "Apartments", "Townhouses" };
        private string[] typeCom = { "Shops", "Warehouse" };
        private string[] legalForms = { "Ownership", "Tenement", "Rental" };
        private List<Estate> estates = new List<Estate>();
        private int id = 0, currEditEstateID;

        public Form1()
        {
            InitializeComponent();
            SetListViewColumns();
            PopulateCombobox(false, -1);
        }

        private void SetListViewColumns()
        {
            listView1.View = View.Details;
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
            if (edit)
                PopulateType(cat);
        }

        private void PopulateCategory()
        {
            for (int i = 0; i < categories.Length; i++)
            {
                cbCategoryEdit.Items.Add(categories[i]);
                cbCategory.Items.Add(categories[i]);
            }
        }

        private void PopulateType(int cat)
        {
            cbType.Items.Clear();
            if (cat == 0)
                for (int i = 0; i < typeRes.Length; i++)
                {
                    cbType.Items.Add(typeRes[i]);
                    cbTypeEdit.Items.Add(typeRes[i]);
                }
            else if (cat == 1)
                for (int i = 0; i < typeCom.Length; i++)
                {
                    cbType.Items.Add(typeCom[i]);
                    cbTypeEdit.Items.Add(typeCom[i]);
                }
        }

        private void PopulateLegalForm()
        {
            for (int i = 0; i < legalForms.Length; i++)
            {
                cbLegalFormEdit.Items.Add(legalForms[i]);
                cbLegalForm.Items.Add(legalForms[i]);
            }
        }

        private void PopulateCountries() {
            foreach (Countries country in Enum.GetValues(typeof(Countries)))
            {
                cbCountry.Items.Add(country);
                cbCountryEdit.Items.Add(country);
            }

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
            listView1.Items.Add(new ListViewItem (estate.Print()));
        }

        private void UpdateListView()
        {
            listView1.Items.Clear();
            foreach (Estate estate in estates)
            {
                listView1.Items.Add(new ListViewItem(estate.Print()));
            }
        }

        private string ChooseImage()
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Filter = "Image files (*.jpg, *.jpeg, *.jpe, *.jfif, *.png) | *.jpg; *.jpeg; *.jpe; *.jfif; *.png";
            if (dlg.ShowDialog() != DialogResult.OK)
                return null;
            return dlg.FileName;
        }

        private Estate GetEstate(int id)
        {
            int i = 0;
            foreach(Estate estate in estates)
            {
                if (estate.ID == id) return estates[i];
                i++;
            }
            return null;
        }

        private Estate CreateEstate(Estate estate, int id, string category, string type, string legalForm, string street, string zipcode, string city, Countries country)
        {           
            estate.ID = id;
            estate.Category = category;
            estate.Type = type;
            estate.LegalForm = legalForm;
            estate.Adress = new Adress(street, zipcode, city, country);

            return estate;
        }

        private void BtnCreate_Click(object sender, EventArgs e)
        {
            string category = cbCategory.SelectedItem.ToString();
            string type = cbType.SelectedItem.ToString();
            string legalForm = cbLegalForm.SelectedItem.ToString();
            string street = tbxStreet.Text;
            string zipcode = tbxZipCode.Text;
            string city = tbxCity.Text;
            Countries country = (Countries)cbCountry.SelectedItem;


            Estate estate = cbCategory.SelectedIndex == 0 ? (Estate) new Residential() : new Commercial();
            estate = CreateEstate(estate, this.id++, category, type, legalForm, street, zipcode, city, country);
            AddToList(estate);
            ClearInput();
        }

        private void cbCategory_SelectedIndexChanged(object sender, EventArgs e)
        {
            int selectedIndex = ((ComboBox)sender).SelectedIndex;
            PopulateType(selectedIndex);
        }

        private void cbCategoryEdit_SelectedIndexChanged(object sender, EventArgs e)
        {
            int selectedIndex = ((ComboBox)sender).SelectedIndex;
            PopulateType(selectedIndex);
        }

        private void btnEdit_Click(object sender, EventArgs e)
        {
            Estate selectedEstate = estates[listView1.SelectedIndices[0]];

            gbxCreate.Enabled = false;
            gbxEdit.Enabled = true;

            int categoryIndex = Array.IndexOf(categories, selectedEstate.Category);
            PopulateType(categoryIndex);

            currEditEstateID = selectedEstate.ID;

            cbCategoryEdit.SelectedIndex = categoryIndex;
            cbTypeEdit.SelectedIndex = categoryIndex == 0 ? Array.IndexOf(typeRes, selectedEstate.Type) : Array.IndexOf(typeCom, selectedEstate.Type);
            cbLegalFormEdit.SelectedIndex = Array.IndexOf(legalForms, selectedEstate.LegalForm); ;
            tbxStreetEdit.Text = selectedEstate.Adress.Street;
            tbxZipCodeEdit.Text = selectedEstate.Adress.Zipcode;
            tbxCityEdit.Text = selectedEstate.Adress.City;
            cbCountryEdit.SelectedIndex = (int)selectedEstate.Adress.Country;
        }

        private void btnRemove_Click(object sender, EventArgs e)
        {
            Estate selectedEstate = estates[listView1.SelectedIndices[0]];
            estates.Remove(selectedEstate);
            listView1.Items.RemoveAt(listView1.SelectedIndices[0]);
        }

        private void btnImage_Click(object sender, EventArgs e)
        {
            lblChosenImage.Text = ChooseImage();
        }

        private void btnConfirmEdit_Click(object sender, EventArgs e)
        {
            try
            {
                Estate selectedEstate = GetEstate(currEditEstateID);

                string category = cbCategoryEdit.SelectedItem.ToString();
                string type = cbTypeEdit.SelectedItem.ToString();
                string legalForm = cbLegalFormEdit.SelectedItem.ToString();
                string street = tbxStreetEdit.Text;
                string zipcode = tbxZipCodeEdit.Text;
                string city = tbxCityEdit.Text;
                Countries country = (Countries)cbCountryEdit.SelectedItem;

                selectedEstate = CreateEstate(selectedEstate, selectedEstate.ID, category, type, legalForm, street, zipcode, city, country);

                UpdateListView();

                ClearInput();

                gbxCreate.Enabled = false;
                gbxEdit.Enabled = true;
            }
            catch(Exception ex)
            {
                MessageBox.Show(ex.ToString());
            }
        }
    }
}
