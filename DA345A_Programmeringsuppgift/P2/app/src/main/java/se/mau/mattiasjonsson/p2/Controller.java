package se.mau.mattiasjonsson.p2;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import se.mau.mattiasjonsson.p2.Connection.ReceiveListener;
import se.mau.mattiasjonsson.p2.Connection.TCPConnection;
import se.mau.mattiasjonsson.p2.Fragments.*;

public class Controller implements Serializable {
    public static final String IP_ADDRESS="10.0.2.2";
    public static final int PORT=7117;
    private TCPConnection connection;
    private boolean connected = false;
    private ReceiveListener listener;
    private Activity activity;
    private Fragment fragment;
    private JSONConverter jsonConverter;
    private String user, group, userID, imageID;
    private ArrayList<Object> unreadMessages = new ArrayList<>();
    private int unreadMessagesPorts;
    private Bitmap bitmap;
    private ImageFragment imageFragment;

    public Controller(Activity activity, Fragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
        jsonConverter = new JSONConverter();
        listener =new RL();
        connection = new TCPConnection(IP_ADDRESS,PORT,listener);
    }

    public void setFragment(Fragment fragment) {this.fragment = fragment;}

    public void connect(){
        connection.connect();
        connected=true;
    }

    public void disconnect() {
        if(connected) {
            connection.disconnect();
        }
    }

    public void send(JSONObject jsonObject) {
        try {
            connection.send(jsonObject);
        }catch(NumberFormatException e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    public void uploadImage(Bitmap bitmap){
       this.bitmap=bitmap;
    }

    private String getJSONType(String message){
        try {
            return (String)((JSONObject) new JSONParser().parse(message)).get("type");
        }catch (Exception e){
            Log.d("EXCEPTION", e.toString());
            return "";
        }
    }

    public String getUserID() {
        return userID;
    }

    public ArrayList<Object> getUnreadMessages() {
        return unreadMessages;
    }

    public int getUnreadMessagesPorts() {
        return unreadMessagesPorts;
    }

    public void setImageFragment(String message) {
        imageFragment = new ImageFragment();
        imageFragment.setMessage(message);
    }

    public void setMessages(String response){
        String message = jsonConverter.getMessage(response);
        if(message!="")
            if(fragment instanceof ChatFragment)
                ((ChatFragment) fragment).setMessageList(message);
            else
                unreadMessages.add(message);
    }

    public void setRegisteredGroups(String response){
        ArrayList groups = jsonConverter.getRegisteredGroups(response);
        if(fragment instanceof LoginFragment)
            ((LoginFragment)fragment).setGroupList(groups);
        else if(fragment instanceof ChangeGroupFragment)
            ((ChangeGroupFragment)fragment).setGroupList(groups);
    }

    public void setRegisteredUser(String response){
        Object[] objects = jsonConverter.registerUserReceiver(response);
        userID = (String)objects[0];
        group = (String)objects[1];
        Log.d("userID", userID);
    }

    public void setUserLocation(String response){
        Object[] objects = jsonConverter.userLocationsReceiver(response);
        if(fragment instanceof MapFragment)
            ((MapFragment)fragment).updateUserLocations((String)objects[0], (HashMap<String, LatLng>) objects[1]);
    }

    public void uploadReceiver(String response){
        Object[] objects = jsonConverter.uploadReceiver(response);
        int port = Integer.parseInt((String)objects[0]);
        imageID = (String)objects[1];
        connection.uploadImage(bitmap, imageID, port);
    }

    public void setImageMessages(String response){
        Object[] objects = jsonConverter.downloadReceiver(response);
        String member = (String)objects[1];
        String message = (String)objects[2];
        String imageid = (String)objects[5];
        String port = (String)objects[6];
        String msg = "From: "+member+"\n"+message+"\nNew image:["+imageid+"]";
        if(fragment instanceof ChatFragment) {
            ((ChatFragment) fragment).setDownloadPort(Integer.parseInt(port));
            ((ChatFragment) fragment).setMessageList(msg);
        }
        else {
            unreadMessagesPorts = Integer.parseInt(port);
            unreadMessages.add(msg);
        }
    }

    public void downloadImage(String imageID, int port){
        try {
            connection.downloadImage(imageID, port);
        }catch(NumberFormatException e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }

    public void setMembers(String response){
        Object[] memberList = jsonConverter.getAllMembersReceiver(response);
        if(fragment instanceof ChangeGroupFragment)
            ((ChangeGroupFragment)fragment).setMemberList((String)memberList[0], (ArrayList<String>)memberList[1]);
    }

    public void changeLanguage(String language) {
        language = (language.equals("EN") ? "sv" : "en");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getApplicationContext().getResources().updateConfiguration(config, null);
        activity.recreate();
    }

    private class RL implements ReceiveListener {
        public void newMessage(final Object response) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (response instanceof String) {
                        String type = getJSONType((String)response);
                        switch (type) {
                            case "groups":setRegisteredGroups((String)response);break;
                            case "register":setRegisteredUser((String)response);break;
                            case "locations":setUserLocation((String)response);break;
                            case "textchat":setMessages((String)response);break;
                            case "imagechat":setImageMessages((String)response);break;
                            case "upload":uploadReceiver((String)response);break;
                            case "members": setMembers((String)response);break;
                        }
                    }
                    else if (response instanceof byte[]){
                        byte[] bitmapdata = (byte[])response;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                        android.support.v4.app.FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                        ft.addToBackStack(null);
                        imageFragment.setContext(fragment.getContext());
                        imageFragment.setBitmap(bitmap);
                        imageFragment.show(ft, "loginFragment");
                    }
                }
            });
        }
    }
}
