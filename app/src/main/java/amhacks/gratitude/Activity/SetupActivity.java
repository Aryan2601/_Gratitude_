package amhacks.gratitude.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import amhacks.gratitude.R;

public class SetupActivity extends AppCompatActivity implements LocationListener {

    private LinearLayout AgeLLT, BasicLLT;
    private EditText AgeET, FullNameET, PhoneET;
    private ImageView AgeTick, ProfileImageView;
    private Spinner GenderSpinner;
    private TextView up_photo;
    private Button SaveButton, button_location;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String currentUserID, profileURL, address;
    private Uri imageUri;
    private StorageReference profileStoreRef;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_setup);

        button_location = findViewById(R.id.location_button);
        
        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid().toString();
        System.out.println(currentUserID);
        firestore = FirebaseFirestore.getInstance();
        profileStoreRef = FirebaseStorage.getInstance().getReference("Profile Images");

        AgeLLT = (LinearLayout) findViewById(R.id.age_llt);
        BasicLLT = (LinearLayout) findViewById(R.id.basic_llt);
        AgeET = (EditText) findViewById(R.id.setup_age_et);
        AgeTick = (ImageView) findViewById(R.id.age_tick);
        FullNameET = (EditText) findViewById(R.id.setup_fullname_et);
        PhoneET = (EditText) findViewById(R.id.setup_phone_et);
        GenderSpinner = (Spinner) findViewById(R.id.setup_gender_et);
        ProfileImageView = (ImageView) findViewById(R.id.setup_profile_image);
        SaveButton = (Button) findViewById(R.id.setup_save_button);


        if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(SetupActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        up_photo = (TextView) findViewById(R.id.up_photo_tv) ;




        AgeTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((AgeET.getText().toString()).isEmpty()))
                {
                    AgeLLT.setVisibility(View.GONE);
                    BasicLLT.setVisibility(View.VISIBLE);
                }
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        ProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    @SuppressLint("MissingPermission")
    private void getlocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,SetupActivity.this);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                Images.Media.INTERNAL_CONTENT_URI);

       intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            ProfileImageView.setImageURI(imageUri);
            verifyImage();


        }
    }

    private void uploadImage() {

        profileStoreRef = profileStoreRef.child(currentUserID);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.show();

        profileStoreRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(SetupActivity.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                        profileStoreRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                               profileURL = uri.toString();
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SetupActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double percentage = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) percentage + "%");
                    }
                });
    }

    void save()
    {
        String fullname, gender, phone;
        fullname = FullNameET.getText().toString();
        gender = GenderSpinner.getSelectedItem().toString();
        phone = PhoneET.getText().toString();

        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || imageUri==null)
        {
            Toast.makeText(this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
        }
        else
        {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fullname", fullname);
            hashMap.put("gender", gender);
            hashMap.put("phone", phone);
            hashMap.put("address", address);
            hashMap.put("profile_picture", profileURL);

            firestore.collection("Users").document(currentUserID).set(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(SetupActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                                Intent mainIntent = new Intent(SetupActivity.this, Dashboard.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                            }
                            else
                            {
                                String err = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, err, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }



    @Override
    public void onLocationChanged(Location location) {

        Toast.makeText(this,""+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();

        try {
            Geocoder gecoder = new Geocoder(SetupActivity.this, Locale.getDefault());
            List<Address> addresses = gecoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            address = addresses.get(0).getAddressLine(0);


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void verifyImage(){


        FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();
        ProfileImageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(ProfileImageView.getDrawingCache());
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        ProfileImageView.setDrawingCacheEnabled(false);
        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(highAccuracyOpts);
        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        // Task completed successfully
                                        // ...
                                        Log.d("MainActivity","Faces detected:"+ Integer.toString(faces.size()));
                                        if(faces.size()==0){
                                            Toast.makeText(SetupActivity.this,"Please insert a proper image",Toast.LENGTH_SHORT).show();

                                        }
                                        else if(faces.size()==1){

                                            up_photo.setVisibility(View.INVISIBLE);
                                            uploadImage();

                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });


    }
}