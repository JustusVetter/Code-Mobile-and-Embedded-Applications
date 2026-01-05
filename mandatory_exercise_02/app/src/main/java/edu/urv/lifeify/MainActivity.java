package edu.urv.lifeify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import edu.urv.lifeify.model.DataSource;
import edu.urv.lifeify.model.DataSourceFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnLogin= findViewById(R.id.btnLogin);
        EditText edtUser = findViewById(R.id.edUserName);
        EditText edtPass = findViewById(R.id.edPasswd);
        Toast wToast = Toast.makeText(this,"wrong", Toast.LENGTH_SHORT);
        Toast cToast = Toast.makeText(this,"valid", Toast.LENGTH_SHORT);
        btnLogin.setOnClickListener( click -> {
                String username = edtUser.getText().toString();
                String password = edtPass.getText().toString();
                String hash = Hashing.sha256().hashString(username+password, StandardCharsets.UTF_8).toString();

                if(hash.equals(getString(R.string.default_password_hash)) && username.equals(getString(R.string.default_user))) {
                    cToast.show();
                    Intent intent = new Intent(MainActivity.this, SectionsActivity.class);
                    startActivity(intent);
                }else{
                    wToast.show();
                }
            }
        );

        DataSource data = DataSourceFactory.getDataSource(DataSource.DataSourceType.HARDCODED);
        data.loadModel();
    }
}