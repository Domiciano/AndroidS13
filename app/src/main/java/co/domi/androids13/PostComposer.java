package co.domi.androids13;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.fragment.app.DialogFragment;

public class PostComposer extends DialogFragment {

    //State
    private String path;

    @Override
    public void onStart() {
        super.onStart();
        //Aseguramos el tamaño del dialog
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //Ponemos el dialogo transparente para poder usar bordes redondos
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.composer, null);
        ImageView galleryImg = root.findViewById(R.id.galleryImg);
        TextView dateTV = root.findViewById(R.id.dateTV);
        Button galleryBtn = root.findViewById(R.id.galleryBtn);
        Button cancelBtn = root.findViewById(R.id.cancelBtn);

        dateTV.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));

        galleryBtn.setOnClickListener(
                v -> {
                    Intent i = new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    getActivity().startActivityForResult(i, MainActivity.GALLERY_CALLBACK);
                }
        );

        cancelBtn.setOnClickListener( v -> dismiss() );

        if(path != null){
            galleryImg.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            galleryImg.setImageBitmap(bitmap);
        }

        return root;
    }

    public void setImagePath(String path) {
        this.path = path;
    }

}
