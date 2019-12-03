package se.mau.mattiasjonsson.p2.Fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import se.mau.mattiasjonsson.p2.Activities.CameraActivity;
import se.mau.mattiasjonsson.p2.Activities.MainActivity;
import se.mau.mattiasjonsson.p2.Controller;
import se.mau.mattiasjonsson.p2.JSONConverter;
import se.mau.mattiasjonsson.p2.R;

import static android.Manifest.permission.CAMERA;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    public static int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA = 1;
    private Button btnImage, btnSend;
    private EditText etMessage;
    private ListView messages_view;
    private Controller controller;
    private JSONConverter jsonConverter;
    private String username;
    private ArrayList<Object> messages = new ArrayList<>();
    private ArrayAdapter<Object> adapter;
    private LatLng location;
    private int downloadPort;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        messages = controller.getUnreadMessages();
        downloadPort = controller.getUnreadMessagesPorts();
        initComponents(view);
        initListeners();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("P2", Activity.MODE_PRIVATE);
        username = sharedPreferences.getString("name", null);
        jsonConverter = new JSONConverter();
        return view;
    }

    private void initComponents(View view){
        btnImage = view.findViewById(R.id.btnImage);
        btnSend = view.findViewById(R.id.btnSend);
        etMessage = view.findViewById(R.id.etMessage);
        messages_view = view.findViewById(R.id.messages_view);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, messages);
        messages_view.setAdapter(adapter);
    }

    private void initListeners(){
        btnSend.setOnClickListener(new ButtonListener());
        btnImage.setOnClickListener(new ButtonListener());
        messages_view.setOnItemClickListener(new ListListener() );
    }

    private void dispatchTakePictureIntent() {
        if(checkCameraHardware(getContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkPermission())
                    requestPermission();
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null)
                this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, REQUEST_CAMERA);
    }
    private boolean checkCameraHardware(Context context) {
        return (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA));
    }

    public void setMessageList(String message){
        messages.add(message);
        adapter.notifyDataSetChanged();
    }

    public void setController(Controller controller) {
        this.controller = controller;
        controller.setFragment(this);
    }

    public void setLocation(LatLng location){
        this.location=location;
    }

    public void setDownloadPort(int downloadPort) {
        this.downloadPort = downloadPort;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            String userID = controller.getUserID();
            String message = etMessage.getText().toString();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Log.d("size", bitmap.getByteCount()+"");
            if(bitmap.getByteCount()<64000) {
                controller.send(jsonConverter.sendImageMessage(userID, message, location));
                controller.uploadImage(bitmap);
                Log.d("test", "image taken");
            }
        }
    }

    private class ListListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String message = (String)messages.get(position);
            String imageID = message.substring(message.indexOf("[")+1, message.indexOf("]"));
            String msg = message.substring(message.indexOf("\n")+1, message.indexOf("\nN"));
            Log.d("Listitem", "ImageID: "+imageID+"\nPort: "+downloadPort);
            controller.setImageFragment(msg);
            controller.downloadImage(imageID, downloadPort);
        }
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnImage:
                    dispatchTakePictureIntent();
                    etMessage.setText("");
                    break;
                case R.id.btnSend:
                    String userID = controller.getUserID();
                    String message = etMessage.getText().toString();
                    controller.send(jsonConverter.sendTextMessage(userID, username, message));
                    etMessage.setText("");
                    break;
            }
        }
    }
}
