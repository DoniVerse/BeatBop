package com.example.beatbop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.beatbop.data.AppDatabase;
import com.example.beatbop.data.entity.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText nameInput, emailInput, passwordInput;
    private TextInputLayout nameLayout, emailLayout, passwordLayout;
    private MaterialButton signupButton;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize database and executor
        db = AppDatabase.getInstance(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();

        // Initialize views
        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        nameLayout = findViewById(R.id.name_layout);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        signupButton = findViewById(R.id.signup_button);

        // Sign Up button click listener
        signupButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                executorService.execute(() -> {
                    // Check if email is already taken
                    if (db.userDao().isEmailTaken(email)) {
                        runOnUiThread(() -> {
                            emailLayout.setError("Email is already registered");
                        });
                        return;
                    }

                    // Create new user
                    User user = new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password); // In a real app, hash the password

                    // Insert user
                    long userId = db.userDao().insert(user);
                    
                    runOnUiThread(() -> {
                        if (userId > 0) {
                            Toast.makeText(SignupActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, MusicListActivity.class);
                            intent.putExtra("USERNAME", name);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });

        findViewById(R.id.login_link).setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInputs() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Reset errors
        nameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);

        boolean isValid = true;

        // Validate name
        if (name.isEmpty()) {
            nameLayout.setError("Name is required");
            isValid = false;
        }

        // Validate email
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email");
            isValid = false;
        }

        // Validate password
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}