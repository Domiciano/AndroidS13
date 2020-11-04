package co.domi.androids13;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    /*
    * !!!!
    * !!!! AL CLONAR ESTE REPOSITORIO, CAMBIEN EL google-services.json
    * !!!! PARA QUE SE CONECTEN A SU PROYECTO DE FIREBASE
    * !!!!
    * */

    public static final int GALLERY_CALLBACK = 1;
    private PostComposer composer;
    private ImageButton newPostBtn;
    private TextView locationTV, bodyTV, dateTV;
    private ImageView lastPostImg;
    private FirebaseStorage storage;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        }, 1);

        newPostBtn = findViewById(R.id.newPostBtn);
        locationTV = findViewById(R.id.locationTV);
        bodyTV = findViewById(R.id.bodyTV);
        dateTV = findViewById(R.id.dateTV);
        lastPostImg = findViewById(R.id.lastPostImg);

        newPostBtn.setOnClickListener(
                v -> {
                    composer = new PostComposer();
                    composer.show(getSupportFragmentManager(), "composer");
                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CALLBACK && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String path = UtilDomi.getPath(this, uri);
            composer.setImagePath(path);
            composer.dismiss();
            composer.show(getSupportFragmentManager(), "composer");
        }

    }
}