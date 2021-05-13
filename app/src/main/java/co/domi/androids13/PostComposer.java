package co.domi.androids13;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PostComposer extends DialogFragment {

    public static final int GALLERY_CALLBACK = 1;

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
        EditText postBodyET = root.findViewById(R.id.postBodyET);
        TextView dateTV = root.findViewById(R.id.dateTV);
        Button galleryBtn = root.findViewById(R.id.galleryBtn);
        Button cancelBtn = root.findViewById(R.id.cancelBtn);
        Button publishBtn = root.findViewById(R.id.publishBtn);

        dateTV.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()));

        galleryBtn.setOnClickListener(
                v -> {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    startActivityForResult(i, GALLERY_CALLBACK);
                }
        );

        publishBtn.setOnClickListener(
                v -> {
                    if (path != null) {
                        try {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            FileInputStream fis = new FileInputStream(new File(path));
                            String name = UUID.randomUUID().toString();
                            storage.getReference().child("post").child(name).putStream(fis)
                                    .addOnSuccessListener(
                                            runnable -> {
                                                Post post = new Post(UUID.randomUUID().toString(), postBodyET.getText().toString(), name);
                                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                                firestore.collection("users")
                                                        .document("admin")
                                                        .collection("posts")
                                                        .document(UUID.randomUUID().toString()).set(post).addOnSuccessListener(
                                                        task -> {
                                                            this.dismiss();
                                                        }
                                                );
                                            }
                                    );
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        cancelBtn.setOnClickListener(v -> dismiss());

        if (path != null) {
            galleryImg.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            galleryImg.setImageBitmap(bitmap);
        }

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CALLBACK && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = UtilDomi.getPath(getActivity(), uri);
            this.path = path;
            this.dismiss();
            this.show(getActivity().getSupportFragmentManager(), "composer");
        }
    }
}
