package edu.urv.lifeify.placeholder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.objectives), (v, insets) -> {
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


        if(section_id.equals("1")) {
            //back.setImageResource(R.drawable.back_health);
            ConstraintLayout back = findViewById(R.id.objectives);
            back.setBackgroundResource(R.drawable.back_health);
            //Log.i("info: ", "back.setImageResource(R.drawable.back_health)");
        }
        else if(section_id.equals("2")){
            //back.setImageResource(R.drawable.back_work);
            ConstraintLayout back = findViewById(R.id.objectives);
            back.setBackgroundResource(R.drawable.back_work);
            Log.i("info: ", "back.setImageResource(R.drawable.back_work)");
        } else{
            //back.setImageResource(R.drawable.back_social);
            ConstraintLayout back = findViewById(R.id.objectives);
            back.setBackgroundResource(R.drawable.back_social);
            Log.i("info: ", "back.setImageResource(R.drawable.back_social)");
        }
    }
}