package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.Internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class setProfile extends AppCompatActivity {

    private CardView getuserimg;
    private ImageView  userimage;
    String name;

    private static int PICK_IMAGE=123;
    private Uri imagepath;

    private EditText username;
    private android.widget.Button saveprofile;

    FirebaseAuth firebaseAuth;

    private FirebaseStorage firebaseStorage;

    private StorageReference storageReference;

    private String ImageUriAccessToken;
    private FirebaseFirestore firebaseFirestore;

    ProgressBar setprofilerProgressBar ;


//we store the data in two data bases to get the benefits of both of them
    // the first database is the realtime database and called here FirebaseDatabase
    // the second database is the cloud firestore and called here FiredatabaseFirestore





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        // so we initialize all our variables

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        // just an instance of storage show that we gonna use the storage
        storageReference= firebaseStorage.getReference();
        //it represents how we gonna save and store the image on our DataBase
        // linked to the fire base storage
        firebaseFirestore = FirebaseFirestore.getInstance();
        // the fire store stores the data in more structured way and you can preform more complex queries on it

        username= findViewById(R.id.getusername);
        getuserimg= findViewById(R.id.getuserimage);
        userimage= findViewById(R.id.getuserimageinimageview);
        saveprofile= findViewById(R.id.saveprofilebtn);
        setprofilerProgressBar= findViewById(R.id.setprofileProgressbar);


        // a method lets you initialize and open the gallery
        getuserimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                // Note that this intent open the gallery of the user not a new activity
                startActivityForResult(intent, PICK_IMAGE);
                // and this method let us get back easily to our activity, with the selected image

            }
        });





        // the process of saving profile data
        saveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = username.getText().toString();

                //assuring the name is not empty
                if(name.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "NAME IS EMPTY", Toast.LENGTH_SHORT).show();

                }
                // assuring the user has selected an image
                if(imagepath==null)
                {

                    Toast.makeText(getApplicationContext(), "IMAGE IS EMPTY", Toast.LENGTH_SHORT).show();
                }
                else // if everything is fine
                {
                    setprofilerProgressBar.setVisibility(View.VISIBLE);
                    sendDataForNewUser();

                    // after sending data hide loading and open the chat activity
                    setprofilerProgressBar.setVisibility(View.INVISIBLE);
                    Intent intent= new Intent(setProfile.this,chatActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });

    }



    private void sendDataForNewUser()
    {
        //to send data to a user, first we will send the data to the realtime database,then to our storage to store the image then to our cloud file store
        sendDataToRealTimeDataBase();

    }



    private void sendDataToRealTimeDataBase()
    {
//realtime database is the only thing that needs a class, storage and cloud firebase don,t need

         name = username.getText().toString().trim();

  //The trim() method removes spaces from both ends of a string when the use enter it


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //A Firebase reference represents a particular location in your Database and can be used for reading or writing data to that Database location

        //firebaseAuth.getUID() method returns the user id by the way that he has been authorized
        // if he authorized by his gmail that will be his id and if he authorized by his phone number that will be the id

        // simply this code creates a new location and the data base identified by the user Id



        UserProfile userProfile = new UserProfile(name, firebaseAuth.getUid());
        // create an object from the user profile class and pass the name and the id to it
        // notice that we haven't also sent the image to the realtime database, cause we store the image at the storage of the firebase

        databaseReference.setValue(userProfile);
        // setting a location to the profile of the user in the realtime database (and its main attributes are the name and the ID)
        Toast.makeText(getApplicationContext(), "USER PROFILE ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
        SendImageToStorage();

    }

    // so we stored the image on the storage and not the realtime database to let the user open the fill image easily
    // if the image was in the realtime database he will only see it as a part of the screen and can't click and open it

    private void SendImageToStorage()
    {

        StorageReference imgreference =  storageReference.child("image").child(firebaseAuth.getUid()).child("profile pic");
        // we create a new reference (location) for the new Image inside th(storageRegrence) location
        // the image will be located inside the folder(child) image and the Id folder(child) and its name will be profile pic
        // remember that storage reference doesn't  represent the storage it self, it represent a location on it so when we create a new object from it, we create a new location


        Bitmap bitmap =null;

        // the images on computer are divided into two types: 1) bitmap images: "images by camera" / 2) vectors images ; images created by graphic designers
        // so we here create an object from the image that its type bitmap

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
            // making the image object to preform functions on it

        }
        catch (IOException e)
        {
            // if we haven't found the image
            e.printStackTrace();
            //it's a method handle the error with indicating the names of the method and line numbers that contain the error
        }


         //we need to compress the photo because photo with high quality images will slow down the app
        //compression the image code

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // the important thing about bytearrayoutputstream it lets you write to multiple files in the same time
        //https://www.javatpoint.com/java-bytearrayoutputstream-class#:~:text=Java%20ByteArrayOutputStream%20class%20is%20used,forwards%20it%20to%20multiple%20streams.

        bitmap.compress(Bitmap.CompressFormat.JPEG, 25 , byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        // tobytestream() method allocate a new bytes of the new size of the image inside the new array that called data here



        //putting image to storage
        UploadTask uploadTask = imgreference.putBytes(data);
        // upload task let us control the process of uploading something like pausing or resuming it
        // and also make functions when some event happens, for example making function displays something when the uploading is done or when it gets failed

        //so this the function that we add to upload task when the uploading is done successfully
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imgreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAccessToken=uri.toString();
                        Toast.makeText(getApplicationContext(), "URI GET SUCCESS", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFileStore();
                    }
                    // and this is the function when it gets failed
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "USI get failed", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "IMAGE IS UPLOADED", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "IMAGE NOT UPLOADED", Toast.LENGTH_SHORT).show();

            }
        });



    }


    private void sendDataToCloudFileStore() {
        DocumentReference documentReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid());
        // the location of the firebase called DocumentReference
        // we store the images inside a collection called users and the name of image will be documented by its id
        // and inside this document we will store these attributes
        Map<String, Object> userdata = new HashMap<>();
        // the hash map store items in "key/value" pairs
        // and the value inserted into it by the put() method
        userdata.put("name", name);
        userdata.put("image", ImageUriAccessToken);
        userdata.put("UID", firebaseAuth.getUid());
        userdata.put("status", "online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "DATA HAS BEEN ADDED TO FIRESTORE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        }); // we can add onfailurelistener if we want


    }








    //show the selected image on our image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode == RESULT_OK)
        {
            //if the user selected an image and the result of this process was totally okay
            imagepath= data.getData();
            //stores the uri of the photo in a variable
            userimage.setImageURI(imagepath);
            //show the image on the image view
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}