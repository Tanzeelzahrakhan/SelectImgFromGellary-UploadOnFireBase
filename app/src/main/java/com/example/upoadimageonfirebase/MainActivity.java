package com.example.upoadimageonfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
ImageView imageView;
Button btnSelect,btnUpload;
Uri ImageUri;
int code=100;
StorageReference storageReference;
ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.image_view);
        btnSelect=findViewById(R.id.btnSelect);
        btnUpload=findViewById(R.id.btnUpload);
         btnSelect.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               /*  Intent intent=new Intent(Intent.ACTION_PICK);
                 intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(intent,code);*/
                 SelectImage();
             }});
         btnUpload.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 UploadImage();}});}
    private void  UploadImage(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Upload Image...");
        progressDialog.show();
        SimpleDateFormat format=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now=new Date();
        String fileName= format.format(now);
        storageReference= FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageView.setImageURI(null);
                Toast.makeText(MainActivity.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void SelectImage(){
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode==RESULT_OK){
            if (requestCode==code){
                imageView.setImageURI(data.getData());
            }
        }*/
        if (requestCode==100 && data!=null && data.getData()!=null){
            ImageUri=data.getData();
            imageView.setImageURI(ImageUri);
        }
    }
}