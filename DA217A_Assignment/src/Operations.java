import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Operations {
	private Connection con = null;
	private Statement stmt = null;
	
	public boolean connect() {
		try {
			con = DriverManager.getConnection("jdbc:postgresql://pgserver.mah.se/ai9852?user=ai9852&password=xcf4o47o");
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void disconnect() {
		try {stmt.close();
		}catch(Exception e){ e.printStackTrace();}
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
	
	public void insert(String tableName, String[] columns, Object[] values) {
		String query="INSERT INTO "+tableName+" (";
		for(int i=0;i<columns.length-1;i++)
			query+=columns[i]+", ";
		query+=columns[columns.length-1]+") VALUES (";
		for(int i=0;i<values.length-1;i++) {
			if(values[i] instanceof String)
				query+="'"+values[i]+"', ";
			else
				query+=values[i]+", ";
		}
		if(values[values.length-1] instanceof String)
			query+="'"+values[values.length-1]+"');";
		else
			query+=values[values.length-1]+");";
		runQuery(query);
	}
	
	public String[][] select(String tableName, String[] columns, String condition) {
		String query = "SELECT ";
		for(int i=0;i<columns.length-1;i++)
			query+=columns[i]+", ";
		query+=columns[columns.length-1]+" FROM "+tableName+" WHERE "+condition+";";
		return runQuery(query);
	}
	
	public String[][] select(String tableName, String[] columns, String condition, String[] orderColumns, int[] order) {
		String[] orderType=getOrder(order);
		String query = "SELECT ";
		for(int i=0;i<columns.length-1;i++)
			query+=columns[i]+", ";
		query+=columns[columns.length-1]+" FROM "+tableName+" WHERE "+condition+" ORDER BY ";
		for(int i=0;i<orderColumns.length-1&&i<order.length-1;i++)
			query += "`"+orderColumns[i]+"` "+order[i]+", ";
		query +=orderColumns[orderColumns.length-1]+" "+orderType[orderType.length-1]+";";
		return runQuery(query);
	}
	
	private String[] getOrder(int[] order) {
		String[] orderType = new String[order.length];
		for(int i=0;i<order.length;i++) 
			switch(order[i]) {
			case 1: orderType[i]="ASC"; break;
			case 2: orderType[i]="DESC"; break;
			}
		return orderType;
	}
	
	public void update(String tableName, String[] columns, String[] values, String condition) {
		String query = "UPDATE "+tableName+" SET ";
		for(int i=0;i<columns.length-1&&i<values.length-1;i++) {
			query +=columns[i]+" = '"+values[i]+"', ";
		}
		query +=columns[columns.length-1]+" = '"+values[values.length-1]+"' "+"WHERE "+condition+";";
		runQuery(query);
	}
	
	public void delete(String tableName, String condition) {
		String query = "DELETE FROM "+tableName+" WHERE "+condition+";";
		runQuery(query); 
	}
	
	public String[][] selectLimit(String tableName, String[] columns, String condition, String[] orderColumns, int[] order, String limit) {
		String[] orderType=getOrder(order);
		String query = "SELECT ";
		for(int i=0;i<columns.length-1;i++)
			query+=columns[i]+", ";
		query+=columns[columns.length-1]+" FROM "+tableName+" WHERE "+condition+" ORDER BY ";
		for(int i=0;i<orderColumns.length-1&&i<order.length-1;i++)
			query += "`"+orderColumns[i]+"` "+order[i]+", ";
		query +=orderColumns[orderColumns.length-1]+" "+orderType[orderType.length-1];
		query+=" LIMIT "+limit+";";
		return runQuery(query);
	}
	
	public String[][] join(int[] type, String[] tables, String[][] columns, String[] conditions, String[] orderColumns, int[] order) {
		String[] joinType=getType(type);
		String query="SELECT ";
		for(int i=0;i<tables.length;i++) 
			for(int j=0;j<columns[i].length;j++) 
				query+=tables[i]+"."+columns[i][j]+", ";
		query=query.substring(0, query.length()-2)+"\nFROM "+tables[0]+"\n";
		for(int i=0;i<joinType.length;i++)
			query+=joinType[i]+" "+tables[i+1]+" ON "+conditions[i]+" ";
		if(orderColumns!=null && order!=null) {
			String[] orderType=getOrder(order);
			query+=" ORDER BY ";
			for(int i=0;i<orderColumns.length-1&&i<orderType.length-1;i++)
				query += "`"+orderColumns[i]+"` "+orderType[i]+", ";
			query += "`"+orderColumns[orderColumns.length-1]+"` "+orderType[orderType.length-1];
		}
		query+=";";
		return runQuery(query);
	}
	
	private String[] getType(int[] type) {
		String[] joinType = new String[type.length];
		for(int i=0;i<type.length;i++) {
			switch(type[i]) {
			case 1: joinType[i]="INNER JOIN";break;
			case 2: joinType[i]="OUTER JOIN";break;
			case 3: joinType[i]="LEFT JOIN";break;
			case 4: joinType[i]="RIGHT JOIN";break;
			}
		}
		return joinType;
	}
}
