package se.mau.mattiasjonsson.p2;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONConverter {

    public JSONObject registerUser(String name, String nameOfGroup){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("type", "register");
            jsonObject.put("group", nameOfGroup);
            jsonObject.put("member", name);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    public JSONObject unregisterUser(String userID){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("type", "unregister");
            jsonObject.put("id", userID);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    public JSONObject sendMyLocation(LatLng location, String userID){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("type", "location");
            jsonObject.put("id", userID);
            jsonObject.put("longitude", String.valueOf(location.longitude));
            jsonObject.put("latitude", String.valueOf(location.latitude));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    public JSONObject sendTextMessage(String userID, String user, String message){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("type", "textchat");
            jsonObject.put("id", userID);
            jsonObject.put("member", user);
            jsonObject.put("text", message);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject sendImageMessage(String userID, String text, LatLng location){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("type", "imagechat");
            jsonObject.put("id", userID);
            jsonObject.put("text", text);
            jsonObject.put("longitude", String.valueOf(location.longitude));
            jsonObject.put("latitude", String.valueOf(location.latitude));
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject fetchRegisteredGroups() {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("type", "groups");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public ArrayList<String> getRegisteredGroups(String message){
        final ArrayList<String> registeredGroups = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray)((JSONObject) parser.parse(message)).get("groups");
            for(int i = 0; i < jsonArray.size(); i++) {
                parser = new JSONParser();
                message = jsonArray.get(i).toString();
                JSONObject group = (JSONObject) parser.parse(message);
                String groupName = (String) group.get("group");
                registeredGroups.add(groupName);
            }
        }catch (Exception ex) {
            Log.d("getRegisteredGroups", ex.toString());
        }
        return registeredGroups;
    }

    public Object[] registerUserReceiver(String message){
        Object[] objects = new Object[2];
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(message);
            objects[0] = jsonObject.get("id");
            objects[1] = jsonObject.get("group");
        } catch (Exception ex) {
            Log.d("EXCEPTION", ex.getMessage());
        }
        return objects;
    }

    public Object[] getAllMembersReceiver(String msg){
        ArrayList<String> arrayListMembers = new ArrayList<>();
        Object[] objects = new Object[2];
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);
            JSONArray jsonArrayAllMembers = (JSONArray) jsonObject.get("members");
            String group = (String)jsonObject.get("group");
            for(int i = 0; i < jsonArrayAllMembers.size(); i++) {
                JSONParser parser = new JSONParser();
                String message = jsonArrayAllMembers.get(i).toString();
                try {
                    JSONObject member = (JSONObject) parser.parse(message);
                    String memberName = (String) member.get("member");
                    arrayListMembers.add(memberName);
                } catch (Exception ex) {
                    Log.d("EXCEPTION", ex.getMessage());
                }
            }
            objects[0]= group;
            objects[1]=arrayListMembers;
        }catch (Exception e){

        }
        return objects;
    }

    public Object[] userLocationsReceiver(String msg){
        Object[] objects = new Object[2];
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);
            final String nameOfGroup = (String) jsonObject.get("group");
            final HashMap<String, LatLng> locations = new HashMap<>();
            JSONArray jsonArrayLocations = (JSONArray) jsonObject.get("location");
            for(int i = 0; i < jsonArrayLocations.size(); i++) {
                String message = jsonArrayLocations.get(i).toString();
                JSONObject group = (JSONObject) new JSONParser().parse(message);
                String memberName = (String) group.get("member");
                String longitude = (String) group.get("longitude");
                String latitude = (String) group.get("latitude");
                LatLng location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                locations.put(memberName, location);
                Log.d("LOCATION", memberName + " in  group: " + nameOfGroup + ": Latitude:" + location.latitude + ", Longitude: " + location.longitude);
            }
            objects[0]=nameOfGroup;
            objects[1]=locations;
        } catch (Exception ex) {
            Log.d("EXCEPTION", ex.getMessage());
        }
        return objects;
    }

    public String getMessage(String msg){
        String message = "";
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);
            if(jsonObject.get("group")!=null)
                message = "From: "+jsonObject.get("member")+" in "+jsonObject.get("group")+"\n"+jsonObject.get("text");
        }catch (Exception e){
            Log.d("EXCEPTION", e.getMessage());
        }
        return message;
    }

    public Object[] uploadReceiver(String msg){
        Object[] objects = new Object[2];
        try{
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);
            objects[0] = jsonObject.get("port");
            objects[1] = jsonObject.get("imageid");
        }catch (Exception e){
            Log.d("EXCEPTION", e.getMessage());
        }
        return objects;
    }

    public Object[] downloadReceiver(String msg){
        Object[] objects = new Object[7];
        try{
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);
            objects[0] = jsonObject.get("group");
            objects[1] = jsonObject.get("member");
            objects[2] = jsonObject.get("text");
            objects[3] = jsonObject.get("longitude");
            objects[4] = jsonObject.get("latitude");
            objects[5] = jsonObject.get("imageid");
            objects[6] = jsonObject.get("port");
        }catch (Exception e){
            Log.d("EXCEPTION", e.getMessage());
        }
        return objects;
    }

    public JSONObject getAllMembers(String groupName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "members");
            jsonObject.put("group", groupName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}