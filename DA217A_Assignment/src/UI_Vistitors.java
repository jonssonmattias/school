import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class UI_Vistitors extends JPanel implements ActionListener{
	
	private JButton btn_scheduele = new JButton("Scheduele");
	private JButton btn_bandInfo = new JButton("Band Info");
	private JButton btn_sceneInfo = new JButton("Scene Info");
	private JTable list = new JTable();
	private final DefaultTableModel model = new DefaultTableModel();
	private JPanel buttonPanel = new JPanel(new BorderLayout());
	private Controller controller = new Controller();
	
	public UI_Vistitors() {
		setLayout(new BorderLayout());
		buttonPanel.add(btn_scheduele, BorderLayout.NORTH);
		buttonPanel.add(btn_bandInfo, BorderLayout.SOUTH);
		buttonPanel.add(btn_sceneInfo,BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.WEST);
		list.setPreferredSize(new Dimension(600,400));
		add(list, BorderLayout.EAST);
		setListeners();
		
		JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        list.setModel((TableModel) model);
        scrollPane.setViewportView(list);
	}
	
	private void setListeners() {
		btn_scheduele.addActionListener(this);
		btn_bandInfo.addActionListener(this);
		btn_sceneInfo.addActionListener(this);
	}

	public static void main(String[] args) {
		UI_Vistitors gui = new UI_Vistitors();
		JFrame frame = new JFrame("GUI");
		frame.setBounds(20, 20, 300, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(gui);
		frame.pack();
	}

	
	public void actionPerformed(ActionEvent e) {
		if(btn_scheduele == e.getSource()) {
			String[][] schedule = controller.getSchedule();
			if(schedule[0].length <= 1) {
				JOptionPane.showMessageDialog(null, "No results found");
				return;
			}
			removeAll(model);
			for(int i=0; i < schedule.length; i++) {
				model.addColumn(schedule[i][0]);
			}
			schedule = convertData(schedule);
			for(int i=0; i < schedule.length; i++) {
				model.addRow(schedule[i]);
			}
		}
		if(btn_bandInfo == e.getSource()) {
			String bandName = JOptionPane.showInputDialog("Choose a band");
			String[][] a = controller.getBandMembers(bandName);
			if(a[0].length <= 1) {
				JOptionPane.showMessageDialog(null, "No members found");
				return;
			}
			String[] bandmembers = a[0];
			
			String[] temp = new String[bandmembers.length-1];	
			for(int i=1;i<bandmembers.length;i++) 
				temp[i-1]=bandmembers[i];
			
			JList list = new JList(temp);
			ListDialog dialog = new ListDialog("Bandmembers of " + bandName, list);
			dialog.show();
		}
		if(btn_sceneInfo == e.getSource()) {
			String sceneName = JOptionPane.showInputDialog("Choose scene");
			String[][] sceneInfo = controller.getSceneInfo(sceneName);
			if(sceneInfo[0].length <= 1) {
				JOptionPane.showMessageDialog(null, "No scenes found");
				return;
			}
			removeAll(model);
			for(int i=0; i < sceneInfo.length; i++) {
				model.addColumn(sceneInfo[i][0]);
			}
			sceneInfo = convertData(sceneInfo);
			for(int i=0; i < sceneInfo.length; i++) {
				model.addRow(sceneInfo[i]);
			}
			
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

	
}

