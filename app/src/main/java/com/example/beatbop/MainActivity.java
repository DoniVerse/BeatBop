package com.example.beatbop;




import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button signupButton;
    TextView loginLink;
//hello worlld

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signupButton = findViewById(R.id.signupButton);
        loginLink = findViewById(R.id.loginButton);

        signupButton.setOnClickListener(v ->{
                Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);}

        );

        loginLink.setOnClickListener(v ->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);}
        );
    }
}
