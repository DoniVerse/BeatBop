package com.example.beatbop;




import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button signupButton;
    TextView loginLink;
//hello

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginButton);

        signupButton.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Sign-up clicked", Toast.LENGTH_SHORT).show()
        );

        loginLink.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Login clicked", Toast.LENGTH_SHORT).show()
        );
    }
}
