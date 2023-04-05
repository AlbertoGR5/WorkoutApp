package express.express.exercise.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.firebaseloginkotlin.R;
import com.example.firebaseloginkotlin.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private static final int FILE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        updateUI();

        binding.updateProfileAppCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.nameEditText.getText().toString();
                updateProfile(name);
            }
        });

        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileManager();
            }
        });

        binding.deleteAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DeleteAccountActivity.class);
                startActivity(intent);
            }
        });

        binding.updatePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.signOutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void updateProfile(String name) {
        FirebaseUser user = auth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Se realizaron los cambios correctamente.", Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    });
        }
    }

    private void fileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, FILE_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_RESULT) {
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
        StorageReference folder = FirebaseStorage.getInstance().getReference().child("Users");
        StorageReference fileName = folder.child("img" + (user != null ? user.getUid() : null));

        fileName.putFile(mUri).addOnSuccessListener(taskSnapshot -> fileName.getDownloadUrl().addOnSuccessListener(uri -> {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();

            if (user != null) {
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Se realizaron los cambios correctamente.", Toast.LENGTH_SHORT).show();
                                updateUI();
                            }
                        });
            }
        })).addOnFailureListener(e -> Log.i("TAG", "file upload error"));
    }

    private void updateUI() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            binding.emailTextView.setText(user.getEmail());

            if (user.getDisplayName() != null) {
                binding.nameTextView.setText(user.getDisplayName());
                binding.nameEditText.setText(user.getDisplayName());
            }

            Glide
                    .with(this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.profileImageView);
            Glide
                    .with(this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.bgProfileImageView);
        }
    }

    private void signOut() {
        auth.signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
