package com.example.authsignin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText signupPassword;
    private EditText signupEmail;
    private EditText signupCourse, signupName;
    ImageView imgView;
    Uri filepath;
    Bitmap bitmap;
    private Button signupButton;
    private Button signupBrowse;
    private FirebaseAuth mAuth;
    private String  getEmailSignup;
    private String getPasswordSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        setView();
        OnClickListener();
    }

    private  void  setView(){
        signupEmail = findViewById(R.id.signupTextEmailAddress);
        signupPassword = findViewById(R.id.signupTextPassword);
        signupButton = findViewById(R.id.signupButton);
        signupName = findViewById(R.id.signupTextPersonName);
        signupCourse = findViewById(R.id.signupEnterCourse);
        imgView = (ImageView)findViewById(R.id.imageView);
        signupBrowse = findViewById(R.id.signupBrowse);
    }

    private void OnClickListener(){
        signupButton.setOnClickListener(view -> {
            getEmailSignup = signupEmail.getEditableText().toString().trim();
            getPasswordSignup = signupPassword.getEditableText().toString().trim();
            add_user();
            uploadToFirebase();
        });

        signupBrowse.setOnClickListener(view -> {
            Dexter.withActivity(MainActivity.this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select image file"),1);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        });

    }

    private void uploadToFirebase() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File uploader");
        dialog.show();



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader= storage.getReference("Image1"+new Random().nextInt(50));

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root = db.getReference("users");

                                dataHolder obj = new dataHolder(signupName.getText().toString(), signupCourse.getText().toString(), uri.toString());
                                root.child(signupCourse.getText().toString()).setValue(obj);
                                signupName.setText("");
                                signupCourse.setText("");
                                imgView.setImageResource(R.drawable.personsignup);
                                Toast.makeText(getApplicationContext(),"Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull  UploadTask.TaskSnapshot snapshot) {
                        float percent = (100* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("uploaded : "+ (int)percent+" %");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        if(requestCode == 1 && resultCode ==RESULT_OK){
            assert data != null;
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void add_user(){
        mAuth.createUserWithEmailAndPassword(getEmailSignup, getPasswordSignup)
                .addOnCompleteListener(MainActivity.this , task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
                        //sending values to other activities
                        intent.putExtra("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                        intent.putExtra("uid", mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "signup unsuccessful", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void GotoLogin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}