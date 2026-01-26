package com.example.weather_app;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TextView city, temperatureValue, windValue, humidityValue, cloudValue, day1, day2, day3, day4, day5, maxDay1, maxDay2, maxDay3, maxDay4, maxDay5, minDay1, minDay2, minDay3, minDay4, minDay5;
    private ImageView imageDay1, imageDay2, imageDay3, imageDay4, imageDay5, currentWeatherImage;

    private Button searchBtn;

    // Blouetooth
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> foundDevices;

    // Multi Threading
    ScheduledExecutorService mExecutor = Executors.newScheduledThreadPool(1);
    Handler mHandler = new Handler(Looper.getMainLooper());
    //Test Toast for Handler
    Toast toast;

    //The textfield for output
    private TextView bluetoothResult;

    //The enable / disable for bluetooth feature
    private Switch bluetoothSwitch;
    private TextInputEditText cityInput;

    private static final String API_KEY = "f70ae9a649de52d2829873e86648c65d";

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
        bluetoothResult = findViewById(R.id.blt_tmp_text);
        bluetoothSwitch = findViewById(R.id.bltswitch);
        city = findViewById(R.id.city);
        temperatureValue = findViewById(R.id.temperature);
        windValue = findViewById(R.id.windValue);
        humidityValue = findViewById(R.id.humidityValue);
        cloudValue = findViewById(R.id.cloudValue);
        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        maxDay1 = findViewById(R.id.maxDay1);
        maxDay2 = findViewById(R.id.maxDay2);
        maxDay3 = findViewById(R.id.maxDay3);
        maxDay4 = findViewById(R.id.maxDay4);
        maxDay5 = findViewById(R.id.maxDay5);
        minDay1 = findViewById(R.id.minDay1);
        minDay2 = findViewById(R.id.minDay2);
        minDay3 = findViewById(R.id.minDay3);
        minDay4 = findViewById(R.id.minDay4);
        minDay5 = findViewById(R.id.minDay5);

        imageDay1 = findViewById(R.id.imageDay1);
        imageDay2 = findViewById(R.id.imageDay2);
        imageDay3 = findViewById(R.id.imageDay3);
        imageDay4 = findViewById(R.id.imageDay4);
        imageDay5 = findViewById(R.id.imageDay5);
        currentWeatherImage = findViewById(R.id.currentWeatherImage);

        searchBtn = findViewById(R.id.searchBtn);

        cityInput = findViewById(R.id.cityInput);

        // Start Multi Threading for Bluetooth
        bluetoothMultiThreading();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityInput.getText().toString();
                if (!city.isEmpty()){
                    FetchWeatherData(city);
                    FetchWeatherNextFiveDays(city);
                }
            }
        });

       FetchWeatherData("Tarragona");
       FetchWeatherNextFiveDays("Tarragona");
    }

     void FetchWeatherData(String city){
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
        executorService.execute(() ->
                {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    try {
                        Response response = client.newCall(request).execute();
                        String result = response.body().string();
                        runOnUiThread(() -> update(result));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
        );
    }

    void FetchWeatherNextFiveDays(String city){
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+ city +"&appid=" + API_KEY + "&units=metric";
        executorService.execute(() ->
                {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    try {
                        Response response = client.newCall(request).execute();
                        String result2 = response.body().string();
                        runOnUiThread(() -> updateFiveDays(result2));
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void update(String result)
    {
        if(result != null)
        {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main =  jsonObject.getJSONObject("main");
                JSONObject clouds =  jsonObject.getJSONObject("clouds");
                double temperature = main.getDouble("temp");
                double humidity = main.getDouble("humidity");
                double cloudPercentage = clouds.getDouble("all");
                double wind= jsonObject.getJSONObject("wind").getDouble("speed");
                String iconCode = jsonObject.getJSONArray("weather")
                        .getJSONObject(0)
                        .getString("icon");

                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                Glide.with(this)
                        .load(iconUrl)
                        .into(currentWeatherImage);
                city.setText(jsonObject.getString("name"));
                temperatureValue.setText(String.format("%.0f°", temperature));
                humidityValue.setText(String.format("%.0f%%", humidity));
                windValue.setText(String.format("%.0f km/h", wind));
                cloudValue.setText(String.format("%.0f%%", cloudPercentage));

            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void updateFiveDays(String result2) {
        if (result2 != null) {
            try {
                JSONObject jsonObject = new JSONObject(result2);
                JSONArray list = jsonObject.getJSONArray("list");
                int[] dayIndices = {0, 8, 16, 24, 32};

                for (int day = 0; day < 5; day++) {
                    JSONObject item = list.getJSONObject(dayIndices[day]);
                    JSONObject main = item.getJSONObject("main");

                    long dt = item.getLong("dt");
                    String dateStr = new java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault())
                            .format(new java.util.Date(dt * 1000));

                    String iconCode = item.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("icon");
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    switch (day) {
                        case 0:
                            Glide.with(this).load(iconUrl).into(imageDay1);
                            day1.setText(dateStr);
                            maxDay1.setText(String.format("%.0f°", main.getDouble("temp_max")));
                            minDay1.setText(String.format("%.0f°", main.getDouble("temp_min")));
                            break;
                        case 1:
                            Glide.with(this).load(iconUrl).into(imageDay2);
                            day2.setText(dateStr);
                            maxDay2.setText(String.format("%.0f°", main.getDouble("temp_max")));
                            minDay2.setText(String.format("%.0f°", main.getDouble("temp_min")));
                            break;
                        case 2:
                            Glide.with(this).load(iconUrl).into(imageDay3);
                            day3.setText(dateStr);
                            maxDay3.setText(String.format("%.0f°", main.getDouble("temp_max")));
                            minDay3.setText(String.format("%.0f°", main.getDouble("temp_min")));
                            break;
                        case 3:
                            Glide.with(this).load(iconUrl).into(imageDay4);
                            day4.setText(dateStr);
                            maxDay4.setText(String.format("%.0f°", main.getDouble("temp_max")));
                            minDay4.setText(String.format("%.0f°", main.getDouble("temp_min")));
                            break;
                        case 4:
                            Glide.with(this).load(iconUrl).into(imageDay5);
                            day5.setText(dateStr);
                            maxDay5.setText(String.format("%.0f°", main.getDouble("temp_max")));
                            minDay5.setText(String.format("%.0f°", main.getDouble("temp_min")));
                            break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // INFO: Code is a chimera from multiple manpage sides.
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("TEST","This is a test");
        if (bluetoothAdapter != null ) {
            Log.d("TEST","B NN");
            if (bluetoothAdapter.isEnabled()) {
                Log.d("TEST","B E");
                bluetoothResult.setVisibility(View.VISIBLE);
                bluetoothSwitch.setVisibility(View.VISIBLE);
            } else if (!bluetoothAdapter.isEnabled()) {
                Log.d("TEST","B D");
                bluetoothResult.setVisibility(View.INVISIBLE);
                bluetoothSwitch.setVisibility(View.INVISIBLE);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void bluetoothMultiThreading(){
        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null ) {
            bluetoothResult.setVisibility(View.INVISIBLE);
            bluetoothSwitch.setVisibility(View.INVISIBLE);
        } else if (!bluetoothAdapter.isEnabled()) {
            bluetoothResult.setVisibility(View.INVISIBLE);
            bluetoothSwitch.setVisibility(View.INVISIBLE);
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        Runnable getAndUpdate = () -> {
            Log.d("TEST", "bluetooth...");
            String lorem = "abc";
            UUID MY_UUID = UUID.randomUUID();
            BluetoothDevice device = null;
            if (device == null){
                Log.d("Device Not Found", "The device wasn't found");
            }else {
                for (BluetoothDevice devices : foundDevices) {
                    if (device.getName().equals("SeeedMaster")) {
                        device = devices;
                    }
                    ;
                }


                BluetoothSocket sock = null;

                try {
                    sock = device.createRfcommSocketToServiceRecord(MY_UUID);
                    sock.connect();
                } catch (IOException e) {
                    Log.d("DEBUG", "Bluetooth socket");
                    try {
                        sock.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                }

                byte[] mmBuffer = new byte[4];
                InputStream instream = null;
                try {
                    instream = sock.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    int numBytes = instream.read(mmBuffer);
                    Log.d("DEBUG READ", "" + mmBuffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                byte b = mmBuffer[0];
            }
            Log.d("Test", "Test");
            // Only for testing
            byte b = (byte) 45;
            Log.d("TEST", " "+ b);
            String be = Byte.toString(b);

            mHandler.post(()->{
                if(bluetoothSwitch.isChecked()) {
                    bluetoothResult.setText(be);
                }else {
                    bluetoothResult.setText("None");
                }
            });
        };
        ScheduledFuture<?> beeperHandle = mExecutor.scheduleWithFixedDelay(getAndUpdate, 5, 5, SECONDS);

    }


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        private Context con;
        {
            con = MainActivity.this; // or pass a valid Context from the outer class
            Log.i("TEST", "search started");
        }


        public void onReceive(Context context, Intent intent) {
            Log.i("TEST", "Device found");

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (ActivityCompat.checkSelfPermission(con,Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permission", "not granted");
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String deviceName = device.getName();

                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d("BlueT", deviceName+ " "+ deviceHardwareAddress);
                foundDevices.add(device);
            }
        }
    };
}