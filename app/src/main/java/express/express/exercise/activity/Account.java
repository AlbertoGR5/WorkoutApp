package express.express.exercise.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.workout.exercise.R;

import express.express.exercise.util.utilhelper;

public class Account extends AppCompatActivity {
    FirebaseAuth auth;
    private final int fileResult = 1;
    View view;
    TextView emailTextView;
    TextView nameTextView;
    EditText nameEditText;
    ImageView profileImageView;
    ImageView bgProfileImageView;
    Button updateProfileAppCompatButton;
    ImageView signOutImageView, backbtn;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        emailTextView = findViewById(R.id.emailTextView);
        nameTextView = findViewById(R.id.nameTextView);
        nameEditText = findViewById(R.id.nameEditText);
        profileImageView = findViewById(R.id.profileImageView);
        bgProfileImageView = findViewById(R.id.bgProfileImageView);
        updateProfileAppCompatButton = findViewById(R.id.updateProfileAppCompatButton);
        signOutImageView = findViewById(R.id.signOutImageView);
        backbtn = findViewById(R.id.back_btn);
        setActionbar("Perfil");

        auth = FirebaseAuth.getInstance();

        updateUI();

        updateProfileAppCompatButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            updateProfile(name);
        });

        signOutImageView.setOnClickListener(v -> signOut());
    }

    private void setActionbar(String title) {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateProfile(String name) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Se realizaron los cambios correctamente.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    });
        }
    }

    private void fileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, fileResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();

                if (uri != null) {
                    imageUpload(uri);
                }
            }
        }
    }

    private void imageUpload(Uri mUri) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            StorageReference folder = FirebaseStorage.getInstance().getReference().child("Users");
            StorageReference fileName = folder.child("img" + user.getUid());

            fileName.putFile(mUri).addOnSuccessListener(taskSnapshot -> fileName.getDownloadUrl().addOnSuccessListener(uri -> {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Se realizaron los cambios correctamente.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI();
                            }
                        });
            })).addOnFailureListener(e -> Log.i("TAG", "error al subir el archivo"));
        }
    }

    private void updateUI() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            emailTextView.setText(user.getEmail());

            if (user.getDisplayName() != null) {
                nameTextView.setText(user.getDisplayName());
                nameEditText.setText(user.getDisplayName());
            }

            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.logito)
                    .into(profileImageView);
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.logito)
                    .into(bgProfileImageView);
        }
    }


    private void signOut() {
        auth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
