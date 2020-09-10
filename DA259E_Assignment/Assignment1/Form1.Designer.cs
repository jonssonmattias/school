namespace Assignment1
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.cbCategory = new System.Windows.Forms.ComboBox();
            this.gbxCreate = new System.Windows.Forms.GroupBox();
            this.cbCountry = new System.Windows.Forms.ComboBox();
            this.tbxCity = new System.Windows.Forms.TextBox();
            this.tbxZipCode = new System.Windows.Forms.TextBox();
            this.tbxStreet = new System.Windows.Forms.TextBox();
            this.lblCountry = new System.Windows.Forms.Label();
            this.lblCity = new System.Windows.Forms.Label();
            this.lblZipCode = new System.Windows.Forms.Label();
            this.lblStreet = new System.Windows.Forms.Label();
            this.btnCreate = new System.Windows.Forms.Button();
            this.cbLegalForm = new System.Windows.Forms.ComboBox();
            this.lblLegalForm = new System.Windows.Forms.Label();
            this.cbType = new System.Windows.Forms.ComboBox();
            this.lblType = new System.Windows.Forms.Label();
            this.lblCategory = new System.Windows.Forms.Label();
            this.gbInfo = new System.Windows.Forms.GroupBox();
            this.btnRemove = new System.Windows.Forms.Button();
            this.btnEdit = new System.Windows.Forms.Button();
            this.btnSearch = new System.Windows.Forms.Button();
            this.tbxSearch = new System.Windows.Forms.TextBox();
            this.lblSearch = new System.Windows.Forms.Label();
            this.listView1 = new System.Windows.Forms.ListView();
            this.gbxEdit = new System.Windows.Forms.GroupBox();
            this.cbCountryEdit = new System.Windows.Forms.ComboBox();
            this.tbxCityEdit = new System.Windows.Forms.TextBox();
            this.tbxZipCodeEdit = new System.Windows.Forms.TextBox();
            this.tbxStreetEdit = new System.Windows.Forms.TextBox();
            this.lblCountryEdit = new System.Windows.Forms.Label();
            this.lblCityEdit = new System.Windows.Forms.Label();
            this.lblZipCodeEdit = new System.Windows.Forms.Label();
            this.lblStreetEdit = new System.Windows.Forms.Label();
            this.btnConfirmEdit = new System.Windows.Forms.Button();
            this.cbLegalFormEdit = new System.Windows.Forms.ComboBox();
            this.lblLegalFormEdit = new System.Windows.Forms.Label();
            this.cbTypeEdit = new System.Windows.Forms.ComboBox();
            this.lblTypeEdit = new System.Windows.Forms.Label();
            this.lblCategoryEdit = new System.Windows.Forms.Label();
            this.cbCategoryEdit = new System.Windows.Forms.ComboBox();
            this.lblImage = new System.Windows.Forms.Label();
            this.btnImage = new System.Windows.Forms.Button();
            this.lblChosenImage = new System.Windows.Forms.Label();
            this.gbxCreate.SuspendLayout();
            this.gbInfo.SuspendLayout();
            this.gbxEdit.SuspendLayout();
            this.SuspendLayout();
            // 
            // cbCategory
            // 
            this.cbCategory.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbCategory.FormattingEnabled = true;
            this.cbCategory.Location = new System.Drawing.Point(105, 21);
            this.cbCategory.Name = "cbCategory";
            this.cbCategory.Size = new System.Drawing.Size(181, 24);
            this.cbCategory.TabIndex = 0;
            this.cbCategory.SelectedIndexChanged += new System.EventHandler(this.cbCategory_SelectedIndexChanged);
            // 
            // gbxCreate
            // 
            this.gbxCreate.Controls.Add(this.lblChosenImage);
            this.gbxCreate.Controls.Add(this.btnImage);
            this.gbxCreate.Controls.Add(this.lblImage);
            this.gbxCreate.Controls.Add(this.cbCountry);
            this.gbxCreate.Controls.Add(this.tbxCity);
            this.gbxCreate.Controls.Add(this.tbxZipCode);
            this.gbxCreate.Controls.Add(this.tbxStreet);
            this.gbxCreate.Controls.Add(this.lblCountry);
            this.gbxCreate.Controls.Add(this.lblCity);
            this.gbxCreate.Controls.Add(this.lblZipCode);
            this.gbxCreate.Controls.Add(this.lblStreet);
            this.gbxCreate.Controls.Add(this.btnCreate);
            this.gbxCreate.Controls.Add(this.cbLegalForm);
            this.gbxCreate.Controls.Add(this.lblLegalForm);
            this.gbxCreate.Controls.Add(this.cbType);
            this.gbxCreate.Controls.Add(this.lblType);
            this.gbxCreate.Controls.Add(this.lblCategory);
            this.gbxCreate.Controls.Add(this.cbCategory);
            this.gbxCreate.Location = new System.Drawing.Point(12, 12);
            this.gbxCreate.Name = "gbxCreate";
            this.gbxCreate.Size = new System.Drawing.Size(292, 332);
            this.gbxCreate.TabIndex = 1;
            this.gbxCreate.TabStop = false;
            this.gbxCreate.Text = "Create Estate";
            // 
            // cbCountry
            // 
            this.cbCountry.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbCountry.FormattingEnabled = true;
            this.cbCountry.Location = new System.Drawing.Point(105, 195);
            this.cbCountry.Name = "cbCountry";
            this.cbCountry.Size = new System.Drawing.Size(181, 24);
            this.cbCountry.TabIndex = 16;
            // 
            // tbxCity
            // 
            this.tbxCity.Location = new System.Drawing.Point(105, 167);
            this.tbxCity.Name = "tbxCity";
            this.tbxCity.Size = new System.Drawing.Size(181, 22);
            this.tbxCity.TabIndex = 15;
            // 
            // tbxZipCode
            // 
            this.tbxZipCode.Location = new System.Drawing.Point(105, 139);
            this.tbxZipCode.Name = "tbxZipCode";
            this.tbxZipCode.Size = new System.Drawing.Size(181, 22);
            this.tbxZipCode.TabIndex = 14;
            // 
            // tbxStreet
            // 
            this.tbxStreet.Location = new System.Drawing.Point(105, 111);
            this.tbxStreet.Name = "tbxStreet";
            this.tbxStreet.Size = new System.Drawing.Size(181, 22);
            this.tbxStreet.TabIndex = 13;
            // 
            // lblCountry
            // 
            this.lblCountry.AutoSize = true;
            this.lblCountry.Location = new System.Drawing.Point(6, 198);
            this.lblCountry.Name = "lblCountry";
            this.lblCountry.Size = new System.Drawing.Size(57, 17);
            this.lblCountry.TabIndex = 12;
            this.lblCountry.Text = "Country";
            // 
            // lblCity
            // 
            this.lblCity.AutoSize = true;
            this.lblCity.Location = new System.Drawing.Point(6, 170);
            this.lblCity.Name = "lblCity";
            this.lblCity.Size = new System.Drawing.Size(31, 17);
            this.lblCity.TabIndex = 11;
            this.lblCity.Text = "City";
            // 
            // lblZipCode
            // 
            this.lblZipCode.AutoSize = true;
            this.lblZipCode.Location = new System.Drawing.Point(3, 142);
            this.lblZipCode.Name = "lblZipCode";
            this.lblZipCode.Size = new System.Drawing.Size(61, 17);
            this.lblZipCode.TabIndex = 9;
            this.lblZipCode.Text = "ZipCode";
            // 
            // lblStreet
            // 
            this.lblStreet.AutoSize = true;
            this.lblStreet.Location = new System.Drawing.Point(6, 114);
            this.lblStreet.Name = "lblStreet";
            this.lblStreet.Size = new System.Drawing.Size(46, 17);
            this.lblStreet.TabIndex = 8;
            this.lblStreet.Text = "Street";
            // 
            // btnCreate
            // 
            this.btnCreate.Location = new System.Drawing.Point(136, 303);
            this.btnCreate.Name = "btnCreate";
            this.btnCreate.Size = new System.Drawing.Size(121, 23);
            this.btnCreate.TabIndex = 6;
            this.btnCreate.Text = "Create";
            this.btnCreate.UseVisualStyleBackColor = true;
            this.btnCreate.Click += new System.EventHandler(this.BtnCreate_Click);
            // 
            // cbLegalForm
            // 
            this.cbLegalForm.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbLegalForm.FormattingEnabled = true;
            this.cbLegalForm.Location = new System.Drawing.Point(105, 81);
            this.cbLegalForm.Name = "cbLegalForm";
            this.cbLegalForm.Size = new System.Drawing.Size(181, 24);
            this.cbLegalForm.TabIndex = 5;
            // 
            // lblLegalForm
            // 
            this.lblLegalForm.AutoSize = true;
            this.lblLegalForm.Location = new System.Drawing.Point(6, 84);
            this.lblLegalForm.Name = "lblLegalForm";
            this.lblLegalForm.Size = new System.Drawing.Size(75, 17);
            this.lblLegalForm.TabIndex = 4;
            this.lblLegalForm.Text = "Legal form";
            // 
            // cbType
            // 
            this.cbType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbType.FormattingEnabled = true;
            this.cbType.Location = new System.Drawing.Point(105, 51);
            this.cbType.Name = "cbType";
            this.cbType.Size = new System.Drawing.Size(181, 24);
            this.cbType.TabIndex = 3;
            // 
            // lblType
            // 
            this.lblType.AutoSize = true;
            this.lblType.Location = new System.Drawing.Point(6, 54);
            this.lblType.Name = "lblType";
            this.lblType.Size = new System.Drawing.Size(40, 17);
            this.lblType.TabIndex = 2;
            this.lblType.Text = "Type";
            // 
            // lblCategory
            // 
            this.lblCategory.AutoSize = true;
            this.lblCategory.Location = new System.Drawing.Point(6, 24);
            this.lblCategory.Name = "lblCategory";
            this.lblCategory.Size = new System.Drawing.Size(65, 17);
            this.lblCategory.TabIndex = 1;
            this.lblCategory.Text = "Category";
            // 
            // gbInfo
            // 
            this.gbInfo.Controls.Add(this.btnRemove);
            this.gbInfo.Controls.Add(this.btnEdit);
            this.gbInfo.Controls.Add(this.btnSearch);
            this.gbInfo.Controls.Add(this.tbxSearch);
            this.gbInfo.Controls.Add(this.lblSearch);
            this.gbInfo.Controls.Add(this.listView1);
            this.gbInfo.Location = new System.Drawing.Point(311, 13);
            this.gbInfo.Name = "gbInfo";
            this.gbInfo.Size = new System.Drawing.Size(791, 534);
            this.gbInfo.TabIndex = 2;
            this.gbInfo.TabStop = false;
            this.gbInfo.Text = "Info";
            // 
            // btnRemove
            // 
            this.btnRemove.Location = new System.Drawing.Point(491, 24);
            this.btnRemove.Name = "btnRemove";
            this.btnRemove.Size = new System.Drawing.Size(75, 23);
            this.btnRemove.TabIndex = 5;
            this.btnRemove.Text = "Remove Item";
            this.btnRemove.UseVisualStyleBackColor = true;
            this.btnRemove.Click += new System.EventHandler(this.btnRemove_Click);
            // 
            // btnEdit
            // 
            this.btnEdit.Location = new System.Drawing.Point(410, 24);
            this.btnEdit.Name = "btnEdit";
            this.btnEdit.Size = new System.Drawing.Size(75, 23);
            this.btnEdit.TabIndex = 4;
            this.btnEdit.Text = "Edit Item";
            this.btnEdit.UseVisualStyleBackColor = true;
            this.btnEdit.Click += new System.EventHandler(this.btnEdit_Click);
            // 
            // btnSearch
            // 
            this.btnSearch.Location = new System.Drawing.Point(329, 24);
            this.btnSearch.Name = "btnSearch";
            this.btnSearch.Size = new System.Drawing.Size(75, 23);
            this.btnSearch.TabIndex = 3;
            this.btnSearch.Text = "Search";
            this.btnSearch.UseVisualStyleBackColor = true;
            // 
            // tbxSearch
            // 
            this.tbxSearch.Location = new System.Drawing.Point(65, 24);
            this.tbxSearch.Name = "tbxSearch";
            this.tbxSearch.Size = new System.Drawing.Size(258, 22);
            this.tbxSearch.TabIndex = 2;
            // 
            // lblSearch
            // 
            this.lblSearch.AutoSize = true;
            this.lblSearch.Location = new System.Drawing.Point(6, 27);
            this.lblSearch.Name = "lblSearch";
            this.lblSearch.Size = new System.Drawing.Size(53, 17);
            this.lblSearch.TabIndex = 1;
            this.lblSearch.Text = "Search";
            // 
            // listView1
            // 
            this.listView1.FullRowSelect = true;
            this.listView1.HideSelection = false;
            this.listView1.Location = new System.Drawing.Point(6, 58);
            this.listView1.Name = "listView1";
            this.listView1.Size = new System.Drawing.Size(779, 462);
            this.listView1.TabIndex = 0;
            this.listView1.UseCompatibleStateImageBehavior = false;
            // 
            // gbxEdit
            // 
            this.gbxEdit.Controls.Add(this.cbCountryEdit);
            this.gbxEdit.Controls.Add(this.tbxCityEdit);
            this.gbxEdit.Controls.Add(this.tbxZipCodeEdit);
            this.gbxEdit.Controls.Add(this.tbxStreetEdit);
            this.gbxEdit.Controls.Add(this.lblCountryEdit);
            this.gbxEdit.Controls.Add(this.lblCityEdit);
            this.gbxEdit.Controls.Add(this.lblZipCodeEdit);
            this.gbxEdit.Controls.Add(this.lblStreetEdit);
            this.gbxEdit.Controls.Add(this.btnConfirmEdit);
            this.gbxEdit.Controls.Add(this.cbLegalFormEdit);
            this.gbxEdit.Controls.Add(this.lblLegalFormEdit);
            this.gbxEdit.Controls.Add(this.cbTypeEdit);
            this.gbxEdit.Controls.Add(this.lblTypeEdit);
            this.gbxEdit.Controls.Add(this.lblCategoryEdit);
            this.gbxEdit.Controls.Add(this.cbCategoryEdit);
            this.gbxEdit.Enabled = false;
            this.gbxEdit.Location = new System.Drawing.Point(12, 350);
            this.gbxEdit.Name = "gbxEdit";
            this.gbxEdit.Size = new System.Drawing.Size(292, 260);
            this.gbxEdit.TabIndex = 17;
            this.gbxEdit.TabStop = false;
            this.gbxEdit.Text = "Edit Estate";
            // 
            // cbCountryEdit
            // 
            this.cbCountryEdit.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbCountryEdit.FormattingEnabled = true;
            this.cbCountryEdit.Location = new System.Drawing.Point(105, 195);
            this.cbCountryEdit.Name = "cbCountryEdit";
            this.cbCountryEdit.Size = new System.Drawing.Size(181, 24);
            this.cbCountryEdit.TabIndex = 17;
            // 
            // tbxCityEdit
            // 
            this.tbxCityEdit.Location = new System.Drawing.Point(105, 167);
            this.tbxCityEdit.Name = "tbxCityEdit";
            this.tbxCityEdit.Size = new System.Drawing.Size(181, 22);
            this.tbxCityEdit.TabIndex = 15;
            // 
            // tbxZipCodeEdit
            // 
            this.tbxZipCodeEdit.Location = new System.Drawing.Point(105, 139);
            this.tbxZipCodeEdit.Name = "tbxZipCodeEdit";
            this.tbxZipCodeEdit.Size = new System.Drawing.Size(181, 22);
            this.tbxZipCodeEdit.TabIndex = 14;
            // 
            // tbxStreetEdit
            // 
            this.tbxStreetEdit.Location = new System.Drawing.Point(105, 111);
            this.tbxStreetEdit.Name = "tbxStreetEdit";
            this.tbxStreetEdit.Size = new System.Drawing.Size(181, 22);
            this.tbxStreetEdit.TabIndex = 13;
            // 
            // lblCountryEdit
            // 
            this.lblCountryEdit.AutoSize = true;
            this.lblCountryEdit.Location = new System.Drawing.Point(3, 198);
            this.lblCountryEdit.Name = "lblCountryEdit";
            this.lblCountryEdit.Size = new System.Drawing.Size(57, 17);
            this.lblCountryEdit.TabIndex = 12;
            this.lblCountryEdit.Text = "Country";
            // 
            // lblCityEdit
            // 
            this.lblCityEdit.AutoSize = true;
            this.lblCityEdit.Location = new System.Drawing.Point(3, 170);
            this.lblCityEdit.Name = "lblCityEdit";
            this.lblCityEdit.Size = new System.Drawing.Size(31, 17);
            this.lblCityEdit.TabIndex = 11;
            this.lblCityEdit.Text = "City";
            // 
            // lblZipCodeEdit
            // 
            this.lblZipCodeEdit.AutoSize = true;
            this.lblZipCodeEdit.Location = new System.Drawing.Point(2, 142);
            this.lblZipCodeEdit.Name = "lblZipCodeEdit";
            this.lblZipCodeEdit.Size = new System.Drawing.Size(61, 17);
            this.lblZipCodeEdit.TabIndex = 9;
            this.lblZipCodeEdit.Text = "ZipCode";
            // 
            // lblStreetEdit
            // 
            this.lblStreetEdit.AutoSize = true;
            this.lblStreetEdit.Location = new System.Drawing.Point(3, 114);
            this.lblStreetEdit.Name = "lblStreetEdit";
            this.lblStreetEdit.Size = new System.Drawing.Size(46, 17);
            this.lblStreetEdit.TabIndex = 8;
            this.lblStreetEdit.Text = "Street";
            // 
            // btnConfirmEdit
            // 
            this.btnConfirmEdit.Location = new System.Drawing.Point(136, 225);
            this.btnConfirmEdit.Name = "btnConfirmEdit";
            this.btnConfirmEdit.Size = new System.Drawing.Size(121, 23);
            this.btnConfirmEdit.TabIndex = 6;
            this.btnConfirmEdit.Text = "Edit";
            this.btnConfirmEdit.UseVisualStyleBackColor = true;
            this.btnConfirmEdit.Click += new System.EventHandler(this.btnConfirmEdit_Click);
            // 
            // cbLegalFormEdit
            // 
            this.cbLegalFormEdit.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbLegalFormEdit.FormattingEnabled = true;
            this.cbLegalFormEdit.Location = new System.Drawing.Point(105, 81);
            this.cbLegalFormEdit.Name = "cbLegalFormEdit";
            this.cbLegalFormEdit.Size = new System.Drawing.Size(181, 24);
            this.cbLegalFormEdit.TabIndex = 5;
            // 
            // lblLegalFormEdit
            // 
            this.lblLegalFormEdit.AutoSize = true;
            this.lblLegalFormEdit.Location = new System.Drawing.Point(6, 84);
            this.lblLegalFormEdit.Name = "lblLegalFormEdit";
            this.lblLegalFormEdit.Size = new System.Drawing.Size(75, 17);
            this.lblLegalFormEdit.TabIndex = 4;
            this.lblLegalFormEdit.Text = "Legal form";
            // 
            // cbTypeEdit
            // 
            this.cbTypeEdit.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbTypeEdit.FormattingEnabled = true;
            this.cbTypeEdit.Location = new System.Drawing.Point(105, 51);
            this.cbTypeEdit.Name = "cbTypeEdit";
            this.cbTypeEdit.Size = new System.Drawing.Size(181, 24);
            this.cbTypeEdit.TabIndex = 3;
            // 
            // lblTypeEdit
            // 
            this.lblTypeEdit.AutoSize = true;
            this.lblTypeEdit.Location = new System.Drawing.Point(6, 54);
            this.lblTypeEdit.Name = "lblTypeEdit";
            this.lblTypeEdit.Size = new System.Drawing.Size(40, 17);
            this.lblTypeEdit.TabIndex = 2;
            this.lblTypeEdit.Text = "Type";
            // 
            // lblCategoryEdit
            // 
            this.lblCategoryEdit.AutoSize = true;
            this.lblCategoryEdit.Location = new System.Drawing.Point(6, 24);
            this.lblCategoryEdit.Name = "lblCategoryEdit";
            this.lblCategoryEdit.Size = new System.Drawing.Size(65, 17);
            this.lblCategoryEdit.TabIndex = 1;
            this.lblCategoryEdit.Text = "Category";
            // 
            // cbCategoryEdit
            // 
            this.cbCategoryEdit.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cbCategoryEdit.FormattingEnabled = true;
            this.cbCategoryEdit.Location = new System.Drawing.Point(105, 21);
            this.cbCategoryEdit.Name = "cbCategoryEdit";
            this.cbCategoryEdit.Size = new System.Drawing.Size(181, 24);
            this.cbCategoryEdit.TabIndex = 0;
            this.cbCategoryEdit.SelectedIndexChanged += new System.EventHandler(this.cbCategoryEdit_SelectedIndexChanged);
            // 
            // lblImage
            // 
            this.lblImage.AutoSize = true;
            this.lblImage.Location = new System.Drawing.Point(9, 233);
            this.lblImage.Name = "lblImage";
            this.lblImage.Size = new System.Drawing.Size(46, 17);
            this.lblImage.TabIndex = 17;
            this.lblImage.Text = "Image";
            // 
            // btnImage
            // 
            this.btnImage.Location = new System.Drawing.Point(105, 233);
            this.btnImage.Name = "btnImage";
            this.btnImage.Size = new System.Drawing.Size(181, 23);
            this.btnImage.TabIndex = 18;
            this.btnImage.Text = "Choose Image";
            this.btnImage.UseVisualStyleBackColor = true;
            this.btnImage.Click += new System.EventHandler(this.btnImage_Click);
            // 
            // lblChosenImage
            // 
            this.lblChosenImage.Location = new System.Drawing.Point(105, 263);
            this.lblChosenImage.Name = "lblChosenImage";
            this.lblChosenImage.Size = new System.Drawing.Size(181, 17);
            this.lblChosenImage.TabIndex = 19;
            this.lblChosenImage.Text = "Image";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1114, 622);
            this.Controls.Add(this.gbxEdit);
            this.Controls.Add(this.gbInfo);
            this.Controls.Add(this.gbxCreate);
            this.Name = "Form1";
            this.Text = "Form1";
            this.gbxCreate.ResumeLayout(false);
            this.gbxCreate.PerformLayout();
            this.gbInfo.ResumeLayout(false);
            this.gbInfo.PerformLayout();
            this.gbxEdit.ResumeLayout(false);
            this.gbxEdit.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ComboBox cbCategory;
        private System.Windows.Forms.GroupBox gbxCreate;
        private System.Windows.Forms.Button btnCreate;
        private System.Windows.Forms.ComboBox cbLegalForm;
        private System.Windows.Forms.Label lblLegalForm;
        private System.Windows.Forms.ComboBox cbType;
        private System.Windows.Forms.Label lblType;
        private System.Windows.Forms.Label lblCategory;
        private System.Windows.Forms.GroupBox gbInfo;
        private System.Windows.Forms.ListView listView1;
        private System.Windows.Forms.TextBox tbxCity;
        private System.Windows.Forms.TextBox tbxZipCode;
        private System.Windows.Forms.TextBox tbxStreet;
        private System.Windows.Forms.Label lblCountry;
        private System.Windows.Forms.Label lblCity;
        private System.Windows.Forms.Label lblZipCode;
        private System.Windows.Forms.Label lblStreet;
        private System.Windows.Forms.Button btnSearch;
        private System.Windows.Forms.TextBox tbxSearch;
        private System.Windows.Forms.Label lblSearch;
        private System.Windows.Forms.GroupBox gbxEdit;
        private System.Windows.Forms.TextBox tbxCityEdit;
        private System.Windows.Forms.TextBox tbxZipCodeEdit;
        private System.Windows.Forms.TextBox tbxStreetEdit;
        private System.Windows.Forms.Label lblCountryEdit;
        private System.Windows.Forms.Label lblCityEdit;
        private System.Windows.Forms.Label lblZipCodeEdit;
        private System.Windows.Forms.Label lblStreetEdit;
        private System.Windows.Forms.Button btnConfirmEdit;
        private System.Windows.Forms.ComboBox cbLegalFormEdit;
        private System.Windows.Forms.Label lblLegalFormEdit;
        private System.Windows.Forms.ComboBox cbTypeEdit;
        private System.Windows.Forms.Label lblTypeEdit;
        private System.Windows.Forms.Label lblCategoryEdit;
        private System.Windows.Forms.ComboBox cbCategoryEdit;
        private System.Windows.Forms.Button btnEdit;
        private System.Windows.Forms.ComboBox cbCountry;
        private System.Windows.Forms.ComboBox cbCountryEdit;
        private System.Windows.Forms.Button btnRemove;
        private System.Windows.Forms.Button btnImage;
        private System.Windows.Forms.Label lblImage;
        private System.Windows.Forms.Label lblChosenImage;
    }
}

