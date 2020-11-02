import java.sql.*;

public class Test {
	private static Connection con = null;
	private Statement stmt = null;
	private static Operations o;
	
	public static void main(String[] args) {
		Test test = new Test();
		o = new Operations();
		test.connect();
		o.connect();

//		test.runQuery("delete from users_online where 1=1");
//		test.runQuery("delete from contacts WHERE u_id > 31 or c_id > 31");
//		test.runQuery("delete from users WHERE user_id > 31");

		String getGroupsAndMembers =
				"select * from users " +
						"inner join groupmembers on users.user_id=groupmembers.u_id "+
						"inner join groups on groupmembers.g_id=groups.group_id ";
						//"where users.username = 'mattias'";

		String[][] result = test.runQuery("select * from contacts");
		//+"AND u_id != (select user_id from users where username = 'Test2')");
		
		int c = result.length, r = result[0].length;
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				System.out.print(result[j][i]+"\t\t");
			}
			System.out.println();
		}
	}
	
	public static void addBandMemberInBand(int bandID, int bandMemberID) {
		o.insert("band_bandMember", new String[] {"bandID", "bandMemberID"}, new Object[] {bandID, bandMemberID});
	}
	
	public static void addSchedule(int sceneID, int bandID, String date, String timeFrom) {
		o.insert("schedule", new String[] {"sceneID", "bandID", "date","timeFrom"}, new Object[] {sceneID, bandID, date, timeFrom});
	}
	
	public static void addChargeOfSecurity(String passID, String workerPN, int sceneID) {
		o.insert("chargeOfSecurity", new String[] {"passID","sceneID","workerPN"}, new Object[]{passID, sceneID, workerPN}); 
	}
	
	public static void addPass(String passID, String date, String timeFrom) {
		o.insert("pass", new String[]{"passID, date, timeFrom"}, new Object[]{passID, date, timeFrom});
	}
	
	public static void addBand(String name, String country, String workerPN) {
		o.insert("band", new String[]{"name, country", "workerPN"}, new Object[] {name, country, workerPN});
	}
	
	public static void addWorker(String workerPN, String firstname, String lastname, String address, boolean isContactPerson, boolean isChargeOfSecurity) {
		o.insert("worker", new String[] {"workerPN", "firstname", "lastname", "address", "isContactPerson", "isChargeOfSecurity"}, new Object[] { workerPN, firstname, lastname, address, isContactPerson, isChargeOfSecurity});
	}
	
	public String[][] select(String tableName, String[] columns, String condition) {
		String query = "SELECT ";
		for(int i=0;i<columns.length-1;i++)
			query+=columns[i]+", ";
		query+=columns[columns.length-1]+" FROM "+tableName+" WHERE "+condition+";";
		return runQuery(query);
	}
	
	
	public boolean connect() {
		try {
			con = DriverManager.getConnection("jdbc:postgresql://xhat-db.cnzyqrtrhsgw.us-east-1.rds.amazonaws.com/postgres",
					"postgres", "cEe9hIxcFfljtgRoRfv3");
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public String[][] runQuery(String query) {
		String[][] s=null;
		try {
			if(!query.toUpperCase().startsWith("SELECT"))
				stmt.execute(query);
			else {
				ResultSet resultSet=stmt.executeQuery(query);  
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				int numberOfColumns = resultSetMetaData.getColumnCount(), numberOfRows = 0;
				while(resultSet.next()) numberOfRows++;
				resultSet.beforeFirst();
				s = new String[numberOfColumns][numberOfRows+1];
				for(int i=0;i<numberOfColumns;i++) {
					int j=0;
					s[i][j++] = resultSetMetaData.getColumnName(i+1);
					while(resultSet.next())
						s[i][j++]=resultSet.getString(i+1);
					resultSet.beforeFirst();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
}
