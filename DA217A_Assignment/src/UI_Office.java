import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class UI_Office {

	private JFrame frame;
	private JComboBox<String> comboBoxScene_BookBand, comboBoxScene_ChargeOfSecurity, comboBoxContactPerson, comboBoxBand, comboBoxChargeOfSecurity, comboBoxPass;
	private final DefaultTableModel model = new DefaultTableModel();
	private Controller controller = new Controller();
	private JTable table;
	private JTextPane tpMembers;
	private JPanel panelWorker, panelScene, panelBand, panelPass, panelBandMembers;
	private HashMap<String, String> chargeOfSecurityPersons = new HashMap<String, String>();
	private HashMap<String, String> contactPersons = new HashMap<String, String>();
	private HashMap<String, String> scenes = new HashMap<String, String>();
	private HashMap<String, String> bands = new HashMap<String, String>();
	private HashMap<String, String> bandMembersList = new HashMap<String, String>();
	private String showTable="";
	private int[] choosenBandmembers;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI_Office window = new UI_Office();
					window.frame.setVisible(true);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI_Office() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Mörtfors Rock Festival");
		frame.setBounds(100, 100, 1157, 946);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		addScene();
		addWorker();
		addBand();
		addBandMember();
		bookBand();
		addPass();
		setChargeOfSecurity();
		getButtons();
		list();		
		populate(controller.getAllContactPersonsName(), "contactPerson");
		populate(controller.getAllScenesName(), "scene");
		populate(controller.getAllBandsName(), "band");
		populate(controller.getAllChargeOfSecurityName(), "chargeOfSecurity");
		populate(controller.getPass(), "pass");
	} 

	private void populate(String[][] result, String type) {
		switch(type) {
		case "scene":
			comboBoxScene_BookBand.removeAllItems();
			comboBoxScene_ChargeOfSecurity.removeAllItems();
			for(int i=1;i<result[0].length;i++)
				scenes.put(result[0][i], result[1][i]);
			for(Object scene : scenes.values().toArray()) {
				comboBoxScene_BookBand.addItem(scene.toString());
				comboBoxScene_ChargeOfSecurity.addItem(scene.toString());
			}
			break;
		case "contactPerson":
			comboBoxContactPerson.removeAllItems();
			for(int i=1;i<result[0].length;i++) 
				contactPersons.put(result[0][i], result[1][i]);
			for(Object contactPerson : contactPersons.values().toArray())
				comboBoxContactPerson.addItem(contactPerson.toString());
			break;
		case "list":
			removeAll(model);
			for(int i=0;i<result.length;i++)
				model.addColumn(result[i][0]);
			result = convertData(result);
			for(int i=0;i<result.length;i++)
				model.addRow(result[i]);
			break;
		case "band":
			comboBoxBand.removeAllItems();
			for(int i=1;i<result[0].length;i++) 
				bands.put(result[0][i], result[1][i]);
			for(Object band : bands.values().toArray())
				comboBoxBand.addItem(band.toString());
			break;
		case "chargeOfSecurity":
			comboBoxChargeOfSecurity.removeAllItems();
			for(int i=1;i<result[0].length;i++) 
				chargeOfSecurityPersons.put(result[0][i], result[1][i]);
			for(Object chargeOfSecurity : chargeOfSecurityPersons.values().toArray())
				comboBoxChargeOfSecurity.addItem(chargeOfSecurity.toString());
			break;
		case "pass":
			for(int i=1;i<result[0].length;i++)
				comboBoxPass.addItem(result[0][i]);
			break;
		}
	}

	private String[][] convertData(String[][] data){
		String[][] res = new String[data[0].length-1][data.length];
		for(int i=1;i<data[0].length;i++) 
			for(int j=0;j<data.length;j++)
				res[i-1][j]=data[j][i];

		return res;
	}

	private void removeAll(DefaultTableModel model) {
		model.setRowCount(0);
		model.setColumnCount(0);
	}

	private void openListDialog(String[][] bandMembers) {	
		String[] temp=new String[bandMembers[0].length];
		for(int i=1;i<bandMembers[0].length;i++)
			temp[i-1]=bandMembers[1][i];
		JList<String> list = new JList<String>(temp);
		ListDialog dialog = new ListDialog("Please select an bandmember in the list: ", list);
		dialog.setOnOk(e -> {
			Object[] memberList = dialog.getSelectedItems();
			choosenBandmembers = dialog.getSelectedIndices();
			tpMembers.setText("");
			for(int i=1;i<memberList.length+1;i++) {
				tpMembers.setText(tpMembers.getText()+memberList[i-1].toString()+"\n");
				bandMembersList.put(Integer.toString(choosenBandmembers[i-1]+1), memberList[i-1].toString());
			}
		});
		dialog.show();

	}

	private void addScene() {
		panelScene = new JPanel();
		panelScene.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Add scene", 0, 0, null, Color.BLACK));
		panelScene.setBounds(12, 13, 240, 170);
		frame.getContentPane().add(panelScene);
		panelScene.setLayout(null);

		JTextField tfName = new JTextField();
		tfName.setBounds(114, 28, 116, 22);
		panelScene.add(tfName);
		tfName.setColumns(10);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 31, 56, 16);
		panelScene.add(lblName);

		JTextField tfMaxAtt = new JTextField();
		tfMaxAtt.setBounds(114, 63, 116, 22);
		panelScene.add(tfMaxAtt);
		tfMaxAtt.setColumns(10);

		JLabel lblMaxAtt = new JLabel("Max Attendance:");
		lblMaxAtt.setBounds(12, 66, 96, 16);
		panelScene.add(lblMaxAtt);

		JTextField tfLocation = new JTextField();
		tfLocation.setBounds(114, 99, 116, 22);
		panelScene.add(tfLocation);
		tfLocation.setColumns(10);

		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(12, 102, 56, 16);
		panelScene.add(lblLocation);

		JButton btnAddScene = new JButton("Add scene");
		btnAddScene.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = tfName.getText();
				String maxAtt = tfMaxAtt.getText();
				String location = tfLocation.getText();

				controller.addScene(name, location, maxAtt);
				populate(controller.getAllScenes(),"list");

				tfName.setText("");
				tfMaxAtt.setText("");
				tfLocation.setText("");
			}
		});
		btnAddScene.setBounds(55, 132, 97, 25);
		panelScene.add(btnAddScene);

	}
	private void addWorker() {
		panelWorker = new JPanel();
		panelWorker.setLayout(null);
		panelWorker.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Add worker", 0, 0, null, Color.BLACK));
		panelWorker.setBounds(12, 196, 240, 245);
		frame.getContentPane().add(panelWorker);

		JTextField tfPN = new JTextField();
		tfPN.setColumns(10);
		tfPN.setBounds(104, 28, 116, 22);
		panelWorker.add(tfPN);

		JLabel lblPN = new JLabel("Person number:");
		lblPN.setBounds(12, 31, 97, 16);
		panelWorker.add(lblPN);

		JTextField tfFirstname = new JTextField();
		tfFirstname.setColumns(10);
		tfFirstname.setBounds(104, 63, 116, 22);
		panelWorker.add(tfFirstname);

		JLabel lblFirstname = new JLabel("Firstname:");
		lblFirstname.setBounds(12, 66, 80, 16);
		panelWorker.add(lblFirstname);

		JTextField tfLastname = new JTextField();
		tfLastname.setColumns(10);
		tfLastname.setBounds(104, 98, 116, 22);
		panelWorker.add(tfLastname);

		JLabel lblLastname = new JLabel("Lastname:");
		lblLastname.setBounds(12, 101, 80, 16);
		panelWorker.add(lblLastname);

		JTextField tfAddress = new JTextField();
		tfAddress.setBounds(104, 133, 116, 22);
		tfAddress.setColumns(10);
		panelWorker.add(tfAddress);

		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setBounds(12, 136, 80, 16);
		panelWorker.add(lblAddress);

		JCheckBox cbContactPerson = new JCheckBox("Is contact person");
		cbContactPerson.setBounds(33, 155, 144, 25);
		panelWorker.add(cbContactPerson);

		JCheckBox cbChargeOfSecurity = new JCheckBox("Is charge of security");
		cbChargeOfSecurity.setBounds(32, 180, 145, 25);
		panelWorker.add(cbChargeOfSecurity);

		JButton btnAddWorker = new JButton("Add worker");
		btnAddWorker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String PN = tfPN.getText();
				String firstname = tfFirstname.getText();
				String lastname = tfLastname.getText();
				String address = tfAddress.getText();
				boolean isContactPerson = cbContactPerson.isSelected();
				boolean isChargeOfSecurity = cbChargeOfSecurity.isSelected();

				controller.addWorker(PN, firstname, lastname, address, isContactPerson, isChargeOfSecurity);
				populate(controller.getAllWorkers(),"list");

				tfPN.setText("");
				tfFirstname.setText("");
				tfLastname.setText("");
				tfAddress.setText("");
				cbContactPerson.setSelected(false);
				cbChargeOfSecurity.setSelected(false);
			}
		});
		btnAddWorker.setBounds(53, 207, 107, 25);
		panelWorker.add(btnAddWorker);
	}
	private void addBand() {
		panelBand = new JPanel();
		panelBand.setLayout(null);
		panelBand.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Add band", 0, 0, null, Color.BLACK));
		panelBand.setBounds(793, 13, 325, 428);
		frame.getContentPane().add(panelBand);

		JTextField tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(125, 28, 188, 22);
		panelBand.add(tfName);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 31, 56, 16);
		panelBand.add(lblName);

		comboBoxContactPerson = new JComboBox<String>();
		comboBoxContactPerson.setBounds(125, 63, 188, 22);
		panelBand.add(comboBoxContactPerson);

		JLabel lblContactPerson = new JLabel("Contact person");
		lblContactPerson.setBounds(12, 66, 101, 16);
		panelBand.add(lblContactPerson);

		JLabel lblCountry = new JLabel("Country:");
		lblCountry.setBounds(12, 101, 56, 16);
		panelBand.add(lblCountry);

		JTextField tfCountry = new JTextField();
		tfCountry.setBounds(125, 98, 188, 22);
		panelBand.add(tfCountry);
		tfCountry.setColumns(10);

		JLabel lblMembers = new JLabel("Members");
		lblMembers.setBounds(12, 130, 56, 16);
		panelBand.add(lblMembers);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(125, 124, 188, 215);
		panelBand.add(scrollPane);

		tpMembers = new JTextPane();
		tpMembers.setEditable(false);
		tpMembers.setBounds(125, 124, 188, 70);
		scrollPane.setViewportView(tpMembers);

		JButton btnChooseBandMember = new JButton("Choose band member");
		btnChooseBandMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openListDialog(controller.getAllBandMembersName());
			}
		});
		btnChooseBandMember.setBounds(23, 352, 277, 25);
		panelBand.add(btnChooseBandMember);		

		JButton btnAddBand = new JButton("Add band");
		btnAddBand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = tfName.getText();
				String workerPN = (String) contactPersons.keySet().toArray()[comboBoxContactPerson.getSelectedIndex()];
				String country = tfCountry.getText();
				String[] bandMembers = tpMembers.getText().split("\n");
				controller.addBand(name, country, workerPN);
				if(bandMembers.length>0) {
					for(int i=0;i<bandMembers.length;i++)
						controller.addBandMemberInBand(Integer.parseInt(controller.getMaxBandID()[0][1]), Integer.parseInt((String) bandMembersList.keySet().toArray()[i]));
					tfName.setText("");
					tfCountry.setText("");
					tpMembers.setText("");
				}
				else JOptionPane.showMessageDialog(null, "Please choose bandmembers");
			}
		});
		btnAddBand.setBounds(23, 390, 277, 25);
		panelBand.add(btnAddBand);
	}
	private void addBandMember() {
		panelBandMembers = new JPanel();
		panelBandMembers.setLayout(null);
		panelBandMembers.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Add band member", 0, 0, null, Color.BLACK));
		panelBandMembers.setBounds(264, 196, 253, 245);
		frame.getContentPane().add(panelBandMembers);

		JTextField tfFirstname = new JTextField();
		tfFirstname.setColumns(10);
		tfFirstname.setBounds(104, 28, 116, 22);
		panelBandMembers.add(tfFirstname);

		JLabel lblFirstname = new JLabel("Firstname");
		lblFirstname.setBounds(12, 31, 80, 16);
		panelBandMembers.add(lblFirstname);

		JTextField tfLastname = new JTextField();
		tfLastname.setColumns(10);
		tfLastname.setBounds(104, 63, 116, 22);
		panelBandMembers.add(tfLastname);

		JLabel lblLastname = new JLabel("Lastname");
		lblLastname.setBounds(12, 66, 80, 16);
		panelBandMembers.add(lblLastname);

		JTextPane tpInfo = new JTextPane();
		tpInfo.setBounds(104, 102, 116, 92);
		panelBandMembers.add(tpInfo);

		JLabel lblInfo = new JLabel("Info:");
		lblInfo.setBounds(12, 102, 56, 16);
		panelBandMembers.add(lblInfo);

		JButton btnAddBandMember = new JButton("Add band member");
		btnAddBandMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String firstname = tfFirstname.getText();
				String lastname = tfLastname.getText();
				String info = tpInfo.getText();

				controller.addBandMember(firstname, lastname, info);

				tfFirstname.setText("");
				tfLastname.setText("");
				tpInfo.setText("");
			}
		});
		btnAddBandMember.setBounds(55, 207, 151, 25);
		panelBandMembers.add(btnAddBandMember);								
	}
	private void bookBand() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Book band", 0, 0, null, Color.BLACK));
		panel.setBounds(529, 196, 253, 245);
		frame.getContentPane().add(panel);

		JLabel lblBand = new JLabel("Band:");
		lblBand.setBounds(12, 31, 80, 16);
		panel.add(lblBand);

		JLabel lblScene = new JLabel("Scene:");
		lblScene.setBounds(12, 66, 80, 16);
		panel.add(lblScene);

		comboBoxScene_BookBand = new JComboBox<String>();
		comboBoxScene_BookBand.setBounds(104, 63, 116, 22);
		panel.add(comboBoxScene_BookBand);

		comboBoxBand = new JComboBox<String>();
		comboBoxBand.setBounds(104, 28, 116, 22);
		panel.add(comboBoxBand);

		JTextField tfTimeFrom = new JTextField();
		tfTimeFrom.setColumns(10);
		tfTimeFrom.setBounds(104, 98, 116, 22);
		panel.add(tfTimeFrom);

		JLabel lblTimeFrom = new JLabel("Time from:");
		lblTimeFrom.setBounds(12, 102, 80, 16);
		panel.add(lblTimeFrom);

		JLabel lblDate = new JLabel("Date: ");
		lblDate.setBounds(12, 136, 56, 16);
		panel.add(lblDate);

		JTextField tfDate = new JTextField();
		tfDate.setBounds(104, 133, 116, 22);
		panel.add(tfDate);
		tfDate.setColumns(10);

		JButton btnBookBand = new JButton("Book band");
		btnBookBand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String timeFrom = tfTimeFrom.getText();
				String date = tfDate.getText();
				int bandID = Integer.parseInt((String) bands.keySet().toArray()[comboBoxBand.getSelectedIndex()]);
				int sceneID = Integer.parseInt((String) scenes.keySet().toArray()[comboBoxScene_BookBand.getSelectedIndex()]);

				controller.addSchedule(sceneID, bandID, date, timeFrom);

				tfTimeFrom.setText("");
				tfDate.setText("");
			}
		});
		btnBookBand.setBounds(61, 207, 97, 25);
		panel.add(btnBookBand);
	}
	private void setChargeOfSecurity() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Set charge of security", 0, 0, null, Color.BLACK));
		panel.setBounds(264, 13, 253, 170);
		frame.getContentPane().add(panel);

		JLabel lblWorker = new JLabel("Worker:");
		lblWorker.setBounds(12, 27, 80, 16);
		panel.add(lblWorker);

		comboBoxChargeOfSecurity = new JComboBox();
		comboBoxChargeOfSecurity.setBounds(104, 24, 116, 22);
		panel.add(comboBoxChargeOfSecurity);

		JLabel lblScene = new JLabel("Scene:");
		lblScene.setBounds(12, 62, 80, 16);
		panel.add(lblScene);

		comboBoxScene_ChargeOfSecurity = new JComboBox();
		comboBoxScene_ChargeOfSecurity.setBounds(104, 59, 116, 22);
		panel.add(comboBoxScene_ChargeOfSecurity);

		JLabel lblPass = new JLabel("Pass:");
		lblPass.setBounds(12, 97, 80, 16);
		panel.add(lblPass);

		comboBoxPass = new JComboBox();
		comboBoxPass.setBounds(104, 94, 116, 22);
		panel.add(comboBoxPass);

		JButton btnSetWorkerIn = new JButton("Set charge of security");
		btnSetWorkerIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String workerPN = (String) chargeOfSecurityPersons.keySet().toArray()[comboBoxChargeOfSecurity.getSelectedIndex()];
				int sceneID = Integer.parseInt((String) scenes.keySet().toArray()[comboBoxScene_ChargeOfSecurity.getSelectedIndex()]);
				String pass = comboBoxPass.getSelectedItem().toString();				
				controller.addChargeOfSecurity(pass, workerPN, sceneID);
			}
		});
		btnSetWorkerIn.setBounds(12, 129, 208, 25);
		panel.add(btnSetWorkerIn);

	}
	private void addPass() {
		panelPass = new JPanel();
		panelPass.setLayout(null);
		panelPass.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Add pass", 0, 0, null, Color.BLACK));
		panelPass.setBounds(529, 13, 253, 170);
		frame.getContentPane().add(panelPass);

		JTextField tfTimeFrom = new JTextField();
		tfTimeFrom.setColumns(10);
		tfTimeFrom.setBounds(104, 65, 116, 22);
		panelPass.add(tfTimeFrom);

		JLabel lblTimeFrom = new JLabel("Time from:");
		lblTimeFrom.setBounds(12, 69, 80, 16);
		panelPass.add(lblTimeFrom);

		JLabel lblDate = new JLabel("Date:");
		lblDate.setBounds(12, 100, 56, 16);
		panelPass.add(lblDate);

		JTextField tfDate = new JTextField();
		tfDate.setBounds(104, 97, 116, 22);
		panelPass.add(tfDate);
		tfDate.setColumns(10);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(12, 34, 80, 16);
		panelPass.add(lblName);

		JTextField tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(104, 30, 116, 22);
		panelPass.add(tfName);

		JButton btnAddPass = new JButton("Add pass");
		btnAddPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String passID = tfName.getText();
				String date = tfDate.getText();
				String timeFrom = tfTimeFrom.getText();
				controller.addPass(passID, date, timeFrom);
				tfName.setText("");
				tfDate.setText("");
				tfTimeFrom.setText("");
			}
		});
		btnAddPass.setBounds(60, 132, 97, 25);
		panelPass.add(btnAddPass);

	}
	private void getButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setBounds(22, 454, 279, 432);
		frame.getContentPane().add(panel);

		JButton btnGetAllContact = new JButton("Get all contact persons");
		btnGetAllContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="contactPerson";
				populate(controller.getContactPersons(),"list");
			}
		});
		btnGetAllContact.setBounds(12, 13, 252, 25);
		panel.add(btnGetAllContact);

		JButton btnGetAllInChargeOfSecurity = new JButton("Get all persons in charge of security");
		btnGetAllInChargeOfSecurity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="chargeOfSecurity";
				populate(controller.getChargeOfSecurity(), "list");
			}
		});
		btnGetAllInChargeOfSecurity.setBounds(12, 51, 252, 25);
		panel.add(btnGetAllInChargeOfSecurity);

		JButton btnEdit = new JButton("Edit selected row");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int num;
				String[] values, columns;
				String key;
				switch(showTable) {
				case "worker":
					num = table.getModel().getColumnCount();
					values = new String[num-1];
					columns = new String[num-1];
					key = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
					for(int i=1;i<num;i++) {
						values[i-1]=table.getModel().getValueAt(table.getSelectedRow(), i).toString();
						columns[i-1]=table.getModel().getColumnName(i);
					}
					controller.update("worker",columns, values, key);
					populate(controller.getAllContactPersonsName(), "contactPerson");
					populate(controller.getAllWorkersName(), "worker");
					populate(controller.getAllWorkers(),"list");
					break;
				case "scene":
					num = table.getModel().getColumnCount();
					values = new String[num-1];
					columns = new String[num-1];
					key = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
					for(int i=1;i<num;i++) {
						values[i-1]=table.getModel().getValueAt(table.getSelectedRow(), i).toString();
						columns[i-1]=table.getModel().getColumnName(i);
					}
					controller.update("scene",columns, values, key);
					populate(controller.getAllScenesName(), "scene");
					populate(controller.getAllScenes(), "list");
					break;
				case "band":
					num = table.getModel().getColumnCount();
					values = new String[num-1];
					columns = new String[num-1];
					key = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
					for(int i=1;i<num;i++) {
						values[i-1]=table.getModel().getValueAt(table.getSelectedRow(), i).toString();
						columns[i-1]=table.getModel().getColumnName(i);
					}
					controller.update("band",columns, values, key);
					populate(controller.getAllBandsName(), "band");
					populate(controller.getAllBands(),"list");
					break;
				case "bandMember":
					num = table.getModel().getColumnCount();
					values = new String[num-1];
					columns = new String[num-1];
					key = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
					for(int i=1;i<num;i++) {
						values[i-1]=table.getModel().getValueAt(table.getSelectedRow(), i).toString();
						columns[i-1]=table.getModel().getColumnName(i);
					}
					controller.update("bandMember",columns, values, key);
					populate(controller.getAllBandMembers(),"list");
					break;
				default:
					if(table.getSelectedRowCount()<1) JOptionPane.showMessageDialog(null, "Please choose a row to edit");
					else JOptionPane.showMessageDialog(null, "Edit not possible");
				}
			}
		});
		btnEdit.setBounds(12, 356, 252, 25);
		panel.add(btnEdit);

		JButton btnDelete = new JButton("Delete selected row");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRowCount()>0) {
					if(table.getSelectedRowCount()<2) {
						String id = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
						switch(showTable) {
						case "worker":
							if(!controller.isChargOfSecurity(id) && !controller.isContactPerson(id)) {							
								controller.remove("worker", new String[] {id} ,0);
								populate(controller.getAllContactPersonsName(), "contactPerson");
								populate(controller.getAllWorkersName(), "worker");
								populate(controller.getAllWorkers(),"list");
							}
							else JOptionPane.showMessageDialog(null, "Error");
							break;
						case "scene":
							controller.remove("chargeOfSecurity", new String[] {id} ,2);
							controller.remove("scene", new String[] {id} ,0);
							populate(controller.getAllScenesName(), "scene");
							populate(controller.getAllScenes(), "list");
							break;
						case "band":
							controller.remove("band_bandMember", new String[] {id}, 1);
							controller.remove("band", new String[] {id} ,0);
							populate(controller.getAllBandsName(), "band");
							populate(controller.getAllBands(),"list");
							break;
						case "bandMember":
							controller.remove("band_bandMember", new String[] {id}, 2);
							controller.remove("bandMember", new String[] {id},0);
							populate(controller.getAllBandMembers(),"list");
							break;
						case "schedule":
							controller.remove("schedule", new String[] {table.getModel().getValueAt(table.getSelectedRow(), 4).toString(), table.getModel().getValueAt(table.getSelectedRow(), 1).toString(), id}, 0);
							populate(controller.getSchedule_Office(),"list");
							break;
						case "pass":
							controller.remove("pass", new String[] {id}, 0);
							populate(controller.getAllPass(),"list");
							break;
						case "membersInBand":
							controller.remove("band_bandMember",new String[] {table.getModel().getValueAt(table.getSelectedRow(), 0).toString(), table.getModel().getValueAt(table.getSelectedRow(), 1).toString()}, 3);
							populate(controller.getAllBandMembersNameInBand(),"list");
							break;
						case "chargeOfSecurity":
							controller.remove("chargeOfSecurity", new String[] {table.getModel().getValueAt(table.getSelectedRow(), 0).toString(),table.getModel().getValueAt(table.getSelectedRow(), 2).toString(),table.getModel().getValueAt(table.getSelectedRow(), 5).toString()} ,4);
							populate(controller.getChargeOfSecurity(),"list");
							break;
						}
					}
					else JOptionPane.showMessageDialog(null, "Only one row can be deleted at once");
				}
				else JOptionPane.showMessageDialog(null, "Please choose a row");
			}});
		btnDelete.setBounds(12, 394, 252, 25);
		panel.add(btnDelete);

		JButton btnAllScenes = new JButton("Get all scenes");
		btnAllScenes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="scene";
				populate(controller.getAllScenes(),"list");
			}
		});
		btnAllScenes.setBounds(12, 89, 252, 25);
		panel.add(btnAllScenes);

		JButton btnAllWorkers = new JButton("Get all workers");
		btnAllWorkers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="worker";
				populate(controller.getAllWorkers(),"list");
			}
		});
		btnAllWorkers.setBounds(12, 127, 252, 25);
		panel.add(btnAllWorkers);

		JButton btnAllBands = new JButton("Get all bands");
		btnAllBands.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="band";
				populate(controller.getAllBands(),"list");
			}
		});
		btnAllBands.setBounds(12, 165, 252, 25);
		panel.add(btnAllBands);

		JButton btnAllBandMembers = new JButton("Get all bandmembers");
		btnAllBandMembers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="bandMember";
				populate(controller.getAllBandMembers(),"list");
			}
		});
		btnAllBandMembers.setBounds(12, 203, 252, 25);
		panel.add(btnAllBandMembers);

		JButton btnPass = new JButton("Get all passes");
		btnPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="pass";
				populate(controller.getAllPass(),"list");
			}
		});
		btnPass.setBounds(12, 241, 252, 25);
		panel.add(btnPass);

		JButton btnSchedule = new JButton("Get schedule");
		btnSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="schedule";
				populate(controller.getSchedule_Office(), "list");
			}
		});
		btnSchedule.setBounds(9, 279, 255, 25);
		panel.add(btnSchedule);
		
		JButton btnMembersInBand = new JButton("Get all members in all band");
		btnMembersInBand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showTable="membersInBand";
				populate(controller.getAllBandMembersNameInBand(), "list");
			}
		});
		btnMembersInBand.setBounds(9, 318, 255, 25);
		panel.add(btnMembersInBand);
	}
	private void list() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.setBounds(317, 454, 801, 393);
		frame.getContentPane().add(panel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 13, 777, 367);
		panel.add(scrollPane);

		table = new JTable();
		table.setModel((TableModel) model);
		scrollPane.setViewportView(table);
	}
}

