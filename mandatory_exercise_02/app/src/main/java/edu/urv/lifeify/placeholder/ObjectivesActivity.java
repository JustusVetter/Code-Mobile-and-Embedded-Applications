package edu.urv.lifeify.placeholder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.urv.lifeify.R;

public class ObjectivesActivity extends AppCompatActivity {

    public String section_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_objectives);

        Intent intent = getIntent();
        section_id = intent.getStringExtra("id");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnExit = findViewById(R.id.btnExit);

        btnExit.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("id", section_id);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}