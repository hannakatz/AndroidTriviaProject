package com.example.triviaproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    private EditText edUsername;
    private EditText edPassword;
    private EditText edVerifyPassword;
    private Button btnCreateUser;
    private ImageView profileImage;
    private Player player;
    private ActivityResultLauncher<Intent> resultLauncherCapture;
    private  ActivityResultLauncher<Intent> resultLauncherPick;
    private RadioButton edMan;
    private RadioButton edFemale;

    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edUsername = findViewById(R.id.ed_username);
        edPassword = findViewById(R.id.ed_password);
        edVerifyPassword = findViewById(R.id.ed_verify_pwd);
        btnCreateUser = findViewById(R.id.btn_create_user);
        profileImage = findViewById(R.id.profile_image);
        edFemale = findViewById(R.id.rbtn_female);
        edMan = findViewById(R.id.rbtn_male);

        resultLauncherCapture = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap)bundle.get("data");
                        profileImage.setBackgroundResource(R.color.transparent);
                        profileImage.setImageBitmap(bitmap);
                    }
                });

        resultLauncherPick = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Intent intent = result.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),intent.getData()
                            );
                            profileImage.setBackgroundResource(R.color.transparent);
                            profileImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setUsername = edUsername.getText().toString();
                String setPassword = edPassword.getText().toString();
                String setVerifyPassword = edVerifyPassword.getText().toString();

                BitmapDrawable convertImageToBitmap = (BitmapDrawable) profileImage.getDrawable();

                if(setPassword.equals("") || setUsername.equals("") || setVerifyPassword.equals("") || convertImageToBitmap == null){
                    Toast.makeText(SignUpActivity.this, "Error! Please complete all data", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (setPassword.equalsIgnoreCase(setVerifyPassword)){

                        Bitmap bitmap = convertImageToBitmap.getBitmap();
                        String setProfileImage = encodeToBase64(bitmap);

                        player = new Player(setUsername,setPassword,setProfileImage, "true");

                        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = credentials.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(player);
                        editor.putString("Player",json);
                        editor.commit();

                        SignUpActivity.this.finish();
                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Error! The passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        selectImage();
                    }
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    resultLauncherCapture.launch(takePicture);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickImageFromGallery = new Intent(Intent.ACTION_PICK);
                    pickImageFromGallery.setType("image/*");
                    resultLauncherPick.launch(pickImageFromGallery);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
        private String encodeToBase64(Bitmap image) {
        Bitmap BitImage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
}