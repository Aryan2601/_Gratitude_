package amhacks.gratitude.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import amhacks.gratitude.R;

public class SetupActivity extends AppCompatActivity {

    private LinearLayout AgeLLT, BasicLLT;
    private EditText AgeET, FullNameET, PhoneET;
    private ImageView AgeTick, ProfileImageView;
    private Spinner GenderSpinner;
    private Button SaveButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String currentUserID, profileURL;
    private Uri imageUri;
    private StorageReference profileStoreRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_setup);
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

    private void selectImage() {
        Intent intent = new Intent();
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
            uploadImage();

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

        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(phone) || imageUri==null)
        {
            Toast.makeText(this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
        }
        else
        {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fullname", fullname);
            hashMap.put("gender", gender);
            hashMap.put("phone", phone);
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
}