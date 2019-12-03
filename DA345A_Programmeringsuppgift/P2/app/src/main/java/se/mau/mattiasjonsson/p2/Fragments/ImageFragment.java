package se.mau.mattiasjonsson.p2.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import se.mau.mattiasjonsson.p2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends DialogFragment {
    private Button btnClose;
    private ImageView imageView;
    private Context context;
    private Bitmap bitmap;
    private TextView tvImageMessage;
    private String message;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        btnClose  = view.findViewById(R.id.btnClose);
        imageView = view.findViewById(R.id.imageView);
        tvImageMessage = view.findViewById(R.id.tvImageMessage);
        tvImageMessage.setText(message);
        btnClose.setOnClickListener(new ButtonListener());
        Drawable d = new BitmapDrawable(context.getResources(), bitmap);
        imageView.setImageDrawable(d);
        return view;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMessage(String message){
        this.message=message;
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

}
