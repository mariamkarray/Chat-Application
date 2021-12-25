package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    EditText mviewusername;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    TextView mmovetoupdateprofile;
    FirebaseFirestore firebaseFirestore;
    ImageView mviewuserimageinimageview;
    StorageReference storageReference;
    private String ImageURIacessToken;
    androidx.appcompat.widget. Toolbar mtoolbarofviewprofile;
    ImageButton mbackbuttonofviewprofile;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mviewuserimageinimageview = findViewById(R.id.viewuserimageinimageview);
        mviewusername = findViewById(R.id.viewusername);
        mmovetoupdateprofile = findViewById(R.id.updateprofile);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mtoolbarofviewprofile = findViewById(R.id.toolbarofviewprofile);
        mbackbuttonofviewprofile = findViewById(R.id.backbuttonofviewprofile);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //making the bar support action like clicking the back button
        setSupportActionBar(mtoolbarofviewprofile);


        //ending the current activity on clicking the back button to return to the previous activity
        mbackbuttonofviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        storageReference = FirebaseStorage.getInstance().getReference();
        //referencing the place where the image is stored in the firestore
        storageReference.child("image").child(firebaseAuth.getUid()).child("profile pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIacessToken = uri.toString();
                //loading the image into the image view using picasso to correct the format and to download the image from the uri(link referring to the image)
                Picasso.get().load(uri).into(mviewuserimageinimageview);
            }
        });

        //getting a reference to the data of the current user to get his name to display it in the text box
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //saving the data of the user inside a snapshot(its like a datatype used to store the data retrieved from the firestore database)
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //getting the data from the snapshot as userprofile datatype with a username and an id
                UserProfile muserprofile = snapshot.getValue(UserProfile.class);

                //setting the username to the textview
                mviewusername.setText(muserprofile.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error message if we can't fetch the data from the firestore
                Toast.makeText (getApplicationContext (), "Failed To Fetch", Toast.LENGTH_SHORT).show();
            }
        });

        //move to a new intent on clicking the text of the updating the profile
        mmovetoupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfile.class);
                startActivity(intent);
            }
        });

    }
}