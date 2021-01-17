package amhacks.gratitude.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import amhacks.gratitude.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileViewActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView usernameTV, emailTV, phoneTV, locationTV;
    private FirebaseAuth mAuth;
    private String username, email, phone, location, currentUserID;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        currentUserID = mAuth.getCurrentUser().getUid().toString();

        profileImage = (CircleImageView) findViewById(R.id.profile_view_image);
        usernameTV = (TextView) findViewById(R.id.profile_view_username);
        emailTV = (TextView) findViewById(R.id.profile_view_email);
        phoneTV = (TextView) findViewById(R.id.profile_view_phone);
        locationTV = (TextView) findViewById(R.id.profile_view_location);

        firestore.collection("Users").document(currentUserID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null)
                        {
                            username = value.get("fullname").toString();
                            email = value.get("email").toString();
                            phone = value.get("phone").toString();
                            location = value.get("address").toString();

                            usernameTV.setText(username);
                            emailTV.setText(email);
                            phoneTV.setText(phone);
                            locationTV.setText(location);

                            Glide.with(getApplicationContext()).load(value.get("profile_picture").toString())
                                    .into(profileImage);


                        }
                    }
                });


    }
}