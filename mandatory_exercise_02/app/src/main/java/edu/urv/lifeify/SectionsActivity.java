package edu.urv.lifeify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.urv.lifeify.model.DataSource;
import edu.urv.lifeify.placeholder.ObjectivesActivity;

public class SectionsActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sections);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String section_id = result.getData().getStringExtra("id");

                        double level = DataSource.getInstance().Recalculate(section_id);
                    }
                }
        );
    }

    protected void OpenObjectives(String section_id) {
        Intent intent = new Intent(this, ObjectivesActivity.class);
        intent.putExtra("id", section_id);
        activityResultLauncher.launch(intent);
    }
}