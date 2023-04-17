package express.express.exercise.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.workout.exercise.databinding.SettingsBinding;

public class Account extends AppCompatActivity {
    private SettingsBinding binding;
    private FirebaseAuth auth;
    private final int fileResult = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = SettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        updateUI();

        binding.updateProfileAppCompatButton.setOnClickListener(v -> {
            String name = binding.nameEditText.getText().toString();
            updateProfile(name);
        });

        binding.profileImageView.setOnClickListener(v -> fileManager());

        binding.signOutImageView.setOnClickListener(v -> signOut());
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
            binding.emailTextView.setText(user.getEmail());

            if (user.getDisplayName() != null) {
                binding.nameTextView.setText(user.getDisplayName());
                binding.nameEditText.setText(user.getDisplayName());
            }

            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.profileImageView);
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.bgProfileImageView);
        }
    }

    private void signOut() {
        auth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
