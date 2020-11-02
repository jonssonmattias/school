
public class Controller {
	private Operations o = new Operations();

	public Controller() {
		o.connect();
	}

	public String[][] getContactPersons() {
		return o.runQuery("select concat(band.workerPN,' ',worker.firstname,' ',worker.lastname) as contactPerson, count(bandMemberID) as NumberOfBandMembers from band_bandMember inner join band on band_bandmember.bandID=band.bandID inner join worker on worker.workerPN=band.workerPN group by concat(band.workerPN,' ',worker.firstname,' ',worker.lastname)");		
	}

	public String[][] getChargeOfSecurity(){
		return o.join(new int[]{1,1,1}, new String[] {"chargeOfSecurity", "scene", "worker", "pass"}, new String[][] {{},{"sceneID","name"}, {"workerPN", "firstname", "lastname"}, {"*"}}, new String[] {"Scene.sceneID=chargeOfSecurity.sceneID","worker.workerPN=chargeOfSecurity.workerPN", "pass.passID=chargeOfSecurity.passID"}, null,null);
	}

	public String[][] getAllScenes(){
		return o.select("scene", new String[] {"*"}, "1=1");
	}

	public String[][] getAllWorkers(){
		return o.select("worker", new String[] {"*"}, "1=1");
	}

	public String[][] getAllBands(){
		return o.select("band", new String[] {"*"}, "1=1");
	}

	public String[][] getAllBandMembers() {
		return o.select("bandmember", new String[] {"*"}, "1=1");
	}

	public String[][] getAllScenesName(){
		return o.select("scene", new String[] {"sceneID", "name"}, "1=1");
	}

	public String[][] getAllContactPersonsName() {
		return o.select("worker", new String[] {"workerPN", "concat(firstname, ' ', lastname)"}, "isContactPerson");
	}

	public String[][] getAllChargeOfSecurityName(){
		return o.select("worker", new String[] {"workerPN", "concat(firstname, ' ', lastname)"}, "isChargeOfSecurity");
	}

	public String[][] getAllWorkersName(){
		return o.select("worker", new String[] {"workerPN", "concat(firstname, ' ', lastname)"}, "isChargeOfSecurity");
	}

	public String[][] getAllBandsName(){
		return o.select("band", new String[] {"bandID", "name"}, "1=1");
	}

	public String[][] getPass(){
		return o.select("pass", new String[] {"passID"}, "1=1");
	}

	public String[][] getAllBandMembersName(){
		return o.select("bandmember", new String[] {"bandMemberID", "concat(firstname, ' ', lastname)"}, "1=1");
	}

	public String[][] getAllBandMembersNameInBand(){
		return o.join(new int[] {1,1}, new String[] {"band_bandMember", "band", "bandMember"}, new String[][] {{"*"},{"name"},{"firstname","lastname"}}, new String[] {"band_bandMember.bandID = band.bandID", "band_bandMember.bandMemberID=bandMember.bandMemberID"}, null, null);
	}

	public String[][] getMaxBandID(){
		return o.selectLimit("band", new String[] {"bandID"}, "1=1", new String[] {"bandID"}, new int[] {2}, "1");
	}

	public String[][] getSchedule(){
		return o.join(new int[]{1,1}, new String[]{"schedule","band","scene"}, new String[][] {{"timeFrom", "date"},{"name"},{"name"}}, new String[]{"band.bandID=schedule.bandID","scene.sceneID=schedule.sceneID"}, null,null);
	}

	public String[][] getSchedule_Office(){
		return o.join(new int[]{1,1}, new String[]{"schedule","band","scene"}, new String[][] {{"timeFrom", "date"},{"name"},{"name","sceneID"}}, new String[]{"band.bandID=schedule.bandID","scene.sceneID=schedule.sceneID"}, null,null);
	}

	public String[][] getAllPass(){
		return o.select("pass", new String[] {"*"}, "1=1");
	}

	public String[][] getBandMembers(String bandName){
		return o.runQuery("select concat(firstname,' ',lastname,' ',infoText) from "
				+ "band_bandMember inner join band on band_bandMember.bandID = band.bandID "
				+ "inner join bandMember on band_bandMember.bandMemberID=bandMember.bandMemberID "
				+ "where band_bandMember.bandID = (select bandID from band where name='"+bandName+"')");
	}

	public String[][] getSceneInfo(String sceneName){
		return o.runQuery("select band.name, schedule.timeFrom, schedule.date from schedule inner join band on band.bandID = schedule.bandID where sceneID = (select sceneID from scene where name ='"+sceneName+"')");
	}

	public boolean isContactPerson(String id) {
		return o.select("band", new String[] {"*"}, "workerPN='"+id+"'")[0].length>1;
	}

	public boolean isChargOfSecurity(String id) {
		return o.select("chargeOfSecurity", new String[] {"*"}, "workerPN='"+id+"'")[0].length>1;
	}


	public void remove(String table, String[] id, int index) {
		switch(table) {
		case "worker": o.delete(table, "workerPN='"+id[0]+"'"); break;
		case "scene": o.delete(table, "sceneID='"+id[0]+"'"); break;
		case "band": o.delete(table, "bandID='"+id[0]+"'"); break;
		case "bandMember": o.delete(table, "bandMemberID='"+id[0]+"'"); break;
		case "chargeOfSecurity": 
			switch(index) {
			case 1: o.delete(table, "workerPN='"+id[0]+"'"); break;
			case 2: o.delete(table, "sceneID='"+id[0]+"'"); break;
			case 3: o.delete(table, "passID='"+id[0]+"'"); break;
			case 4: o.delete(table, "workerPN='"+id[1]+"' and sceneID='"+id[0]+"' and passID='"+id[2]+"'");
			}
			break;
		case "band_bandMember":
			switch(index) {
			case 1: o.delete(table, "bandID="+id[0]); break;
			case 2: o.delete(table, "bandMemberID="+id[0]); break;
			case 3: o.delete(table, "bandMemberID="+id[0]+" and bandID="+id[1]); break;
			}
			break;
		case "schedule": o.delete(table, "sceneID="+id[0]+" and date='"+id[1]+"' and timeFrom='"+id[2]+"';" );
		case "pass": o.delete(table, "passID='"+id[0]+"'");
		}
	}

	public void update(String table,String[] columns, String[] values, String key) {
		switch(table) {
		case "worker": o.update(table, columns, values, "workerPN='"+key+"'");break;
		case "scene": o.update(table, columns, values, "sceneID='"+key+"'"); break;
		case "band": o.update(table, columns, values, "bandID='"+key+"'"); break;
		case "bandMember": o.update(table, columns, values, "bandMemberID='"+key+"'"); break;
		}

	}

	public void addBandMemberInBand(int bandID, int bandMemberID) {
		o.insert("band_bandMember", new String[] {"bandID", "bandMemberID"}, new Object[] {bandID, bandMemberID});
	}

	public void addSchedule(int sceneID, int bandID, String date, String timeFrom) {
		o.insert("schedule", new String[] {"sceneID", "bandID", "date","timeFrom"}, new Object[] {sceneID, bandID, date, timeFrom});
	}

	public void addChargeOfSecurity(String passID, String workerPN, int sceneID) {
		o.insert("chargeOfSecurity", new String[] {"passID","sceneID","workerPN"}, new Object[]{passID, sceneID, workerPN}); 
	}

	public void addPass(String passID, String date, String timeFrom) {
		o.insert("pass", new String[]{"passID, date, timeFrom"}, new Object[]{passID, date, timeFrom});
	}

	public void addBand(String name, String country, String workerPN) {
		o.insert("band", new String[]{"name, country", "workerPN"}, new Object[] {name, country, workerPN});
	}

	public void addWorker(String workerPN, String firstname, String lastname, String address, boolean isContactPerson, boolean isChargeOfSecurity) {
		o.insert("worker", new String[] {"workerPN", "firstname", "lastname", "address", "isContactPerson", "isChargeOfSecurity"}, new Object[] { workerPN, firstname, lastname, address, isContactPerson, isChargeOfSecurity});
	}

	public void addScene(String name, String location, String maxAtt) {
		o.insert("scene", new String[] {"name", "location", "maxAttendance"}, new Object[] {name, location, maxAtt});
	}

	public void addBandMember(String firstname, String lastname, String info) {
		o.insert("bandMember", new String[] {"firstname","lastname", "infoText"}, new String[] {firstname, lastname, info} );
	}
}
