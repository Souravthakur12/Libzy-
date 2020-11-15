package abvgiet.library.libzy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import abvgiet.library.libzy.module.UserActivity;

public class Edit_User_Details extends AppCompatActivity {

    ImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESTCODE = 1 ;
    Uri pickedImgUri ;

    private EditText userName,userStream,userPhone,userRoll_No;
    private ProgressBar loadingProgress;
    private Button regBtn;


    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__user__details);






        //init firebase

        firebaseAuth = FirebaseAuth.getInstance();


        database =FirebaseDatabase.getInstance();
        reference = database.getReference("Users");


        //init views

        userName = findViewById(R.id.regName);
        userStream = findViewById(R.id.regStream);
        userPhone = findViewById(R.id.regPhone);
        userRoll_No = findViewById(R.id.regRollNo);
        regBtn = findViewById(R.id.regBtn);
        loadingProgress = findViewById(R.id.regProgressBar);



        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String name = userName.getText().toString();
                final String stream = userStream.getText().toString();
                final String phone = userPhone.getText().toString();
                final String roll = userRoll_No.getText().toString();

                if (name.isEmpty() || stream.isEmpty() || phone.isEmpty() || roll.isEmpty()){

                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields") ;
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }
                else{
                    UploadUserDetails();


                    if (pickedImgUri != null){
                        UploadUserPhoto( name ,pickedImgUri,firebaseAuth.getCurrentUser());
                    }
                    else {
                        UploadUserWithoutPhoto(name,firebaseAuth.getCurrentUser());
                    }



                   Intent i = new Intent(Edit_User_Details.this, UserActivity.class);
                    startActivity(i);


                }




            }
        });



        ImgUserPhoto = findViewById(R.id.regUserPhoto) ;

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();


                }
                else
                {

                    openGallery();


                }


            }
        });





    }

    private void UploadUserPhoto(final String userName , final Uri pickedImgUri, final FirebaseUser currentUser) {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully


                                            showMessage("Register Complete");

                                        }

                                    }
                                });

                    }
                });





            }
        });





    }
    private void UploadUserWithoutPhoto(final String userName, final FirebaseUser currentUser) {




                        // uri contain user image url


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();


                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully


                                            showMessage("Register Complete");

                                        }

                                    }
                                });

                    }








    private void  UploadUserDetails() {
        String name = userName.getText().toString();
        String stream = userStream.getText().toString();
        String phone = userPhone.getText().toString();
        String roll = userRoll_No.getText().toString();
      //  String imguser = pickedImgUri.toString();

        HashMap<String,Object> result = new HashMap<>();
        result.put("name",name);
        result.put("stream",stream);
        result.put("phone",phone);
        result.put("roll_no",roll);
      //  result.put("image",imguser);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        reference.child(user.getUid()).updateChildren(result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(getApplication(),"Updated",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplication(),"Error !! "+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

    }




    private void showMessage(String please_verify_all_fields) {
    }

    private void openGallery() {

        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);

    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Edit_User_Details.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Edit_User_Details.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Edit_User_Details.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(Edit_User_Details.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);


        }


    }






    }


