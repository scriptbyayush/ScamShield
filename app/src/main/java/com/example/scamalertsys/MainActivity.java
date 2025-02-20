package com.example.scamalertsys;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        DualCircularProgressBar dualProgressBar = findViewById(R.id.dualProgressBar);
// Example: Update progress to 30 scam calls and 70 normal calls with animation.
        dualProgressBar.setProgressAnimated(4, 16);


        RealTimeProgress realTimeProgress = findViewById(R.id.dualProgressBar2);
// Example: Update progress to 30 scam calls and 70 normal calls with animation.
        realTimeProgress.setProgressAnimated(11, 15);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}