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
import android.os.ParcelUuid;
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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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

    BluetoothSocket sock = null;
    BluetoothDevice device = null;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> foundDevices = new ArrayList<>();

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
                    String dateStr = new SimpleDateFormat("EEE", Locale.getDefault())
                            .format(new Date(dt * 1000));

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




        //boolean started = bluetoothAdapter.startDiscovery();
        //Log.d("BT", "Discovery started: " + started);

        Runnable getAndUpdate = () -> {
            Log.d("TESTINGTHESHIT", "bluetooth...");
            byte b = (byte) 6;
            if(sock == null) {
                Log.d("TEST", "bluetooth...");
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Permission scan not granted");
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults);
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("PERMISSION","Permission ACCESS_FINE_LOCATION not granted" );
                    return;
                }
                try {

                    bluetoothAdapter.startDiscovery();


                } catch (Exception e) {
                    Log.d("FAILED", "DISCOVERY FAILED");
                    throw new RuntimeException(e);
                }

                UUID MY_UUID = UUID.randomUUID();



                //Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                try {
            /*if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice btdevice : pairedDevices) {
                    String deviceName = btdevice.getName();
                    String deviceHardwareAddress = btdevice.getAddress(); // MAC address
                    if ("SeeedMaster".equals(btdevice.getName())) {
                        Log.d("SEEED", "SeeedMaster found");
                        device = btdevice;
                        Log.i("DEVICE INFO 1", "" + device.getName());
                        Log.i("DEVICE INFO 3", "" + device.getAddress());
                        ParcelUuid[] uuids = device.getUuids();
                        if (uuids != null) {
                            Log.i("DEVICE INFO 4", "UUIDs: " + Arrays.toString(uuids));
                            MY_UUID = uuids[0].getUuid();
                            Log.i("My DEVICE INFO 1", "UUID: " + MY_UUID);
                        } else {
                            Log.i("DEVICE INFO 4", "UUIDs: null");
                        }
                        if (bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }

                    }
                }
            }*/
                    List<BluetoothDevice> copyDevices = foundDevices;
                    foundDevices = new ArrayList<>();
                    if (copyDevices.isEmpty()) {
                        Log.d("Device Not Found", "The device wasn't found");
                    } else {

                        for (BluetoothDevice fDevice : copyDevices) {
                            Log.d("DEVICES", "length: " + copyDevices.size());
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                Log.i("PERMISSION", "Bluetooth Connect Missing");
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                ActivityCompat.requestPermissions(
                                        this,
                                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                                        101
                                );
                                //return;
                            }
                            if ("SeeedMaster".equals(fDevice.getName())) {
                                Log.d("SEEED", "SeeedMaster found");
                                device = fDevice;
                                Log.i("DEVICE INFO 1", "" + device.getName());
                                Log.i("DEVICE INFO 3", "" + device.getAddress());
                                ParcelUuid[] uuids = device.getUuids();
                                if (uuids != null) {
                                    Log.i("DEVICE INFO 4", "UUIDs: " + Arrays.toString(uuids));
                                    MY_UUID = uuids[0].getUuid();
                                    Log.i("My DEVICE INFO 1", "UUID: " + MY_UUID);
                                } else {
                                    Log.i("DEVICE INFO 4", "UUIDs: null");
                                }
                                if (bluetoothAdapter.isDiscovering()) {
                                    bluetoothAdapter.cancelDiscovery();
                                }
                                unregisterReceiver(receiver);

                            }
                        }
                    }
                    if (device != null) {
                        Log.i("DEVICE INFO 2", "device.getName() == null? " + (device.getName() == null));
                    } else {
                        Log.i("DEVICE INFO 2", "device is null — SeeedMaster not found");
                    }

                } catch (Exception e) {
                    Log.e("RECEIVER ERROR", "Exception in onReceive", e);
                }


                try {
                    Log.i("SOCKET", "starting socket...");
                    if(device!=null) {
                        sock = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    }
                    Log.i("SOCKET", "Socket created");
                    if(sock!=null) {
                        sock.connect();

                        Log.d("SOCKET", "Socket Connected: " + sock.isConnected());
                    }
                } catch (IOException e) {
                    Log.d("DEBUG", "Bluetooth socket failed", e);
                    try {
                        sock.close();
                    } catch (IOException ex) {
                        Log.d("DEBUG", "Bluetooth socket failed to close");
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                }
            }else{
                Log.d("AdvanceSocket", ""+sock.isConnected());


            }
            int numBytes =0;

                Log.d("FETCH", "get data");
                byte[] mmBuffer = new byte[51];
                InputStream instream = null;
            if(sock!= null) {
                try {
                    instream = sock.getInputStream();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (sock.isConnected() && instream != null) {
                        int totalRead = 0;

                        while (totalRead < 51) {
                            int n = instream.read(mmBuffer, totalRead, 51 - totalRead);
                            // Check if connection was removed
                            if (n == -1) {
                                throw new IOException("Stream closed before reading 51 bytes");

                            }

                            totalRead += n;
                        }
                        numBytes = totalRead;

                        Log.d("DEBUG READ", "" + numBytes);


                    }
                } catch (IOException e) {
                    Log.e("BIGBT", "Connection lost", e);
                    sock = null;
                    device = null;
                    foundDevices = new ArrayList<>();
                    try {
                        instream.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }


            Log.d("Test", "Test");
            // Only for testing
            //byte b = (byte) 45;

            String be = "1<tmp>None</tmp>1";

            if(sock!=null) {
                if (sock.isConnected()) {
                    be = new String(mmBuffer, 0, numBytes, StandardCharsets.US_ASCII);
                }
            }
            Log.i("DEBUG1", be);

            // get one fully valid xml tag

            int startValidTag=be.indexOf("<tmp>");
            be = be.substring(startValidTag+5);
            Log.i("DEBUG2", be);
            int endValidTag=be.indexOf("</tmp>");
            be = be.substring(0, endValidTag);

            Log.i("DEBUG3", be);


            String finalBe = be;
            mHandler.post(()->{
                if(bluetoothSwitch.isChecked()) {
                    bluetoothResult.setText(finalBe);
                }else {
                    bluetoothResult.setText("None");
                }
            });
        };
        ScheduledFuture<?> beeperHandle = mExecutor.scheduleWithFixedDelay(getAndUpdate, 0, 5, SECONDS);

    }



    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        private Context con;

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("TEST", "Device received");
            con = context;

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice recev_device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


                if (ActivityCompat.checkSelfPermission(con,Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(con,Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
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
                assert recev_device != null;
                String deviceName = recev_device.getName() != null ? recev_device.getName() : "Unknown device";

                String deviceHardwareAddress = recev_device.getAddress(); // MAC address
                Log.d("BlueT", deviceName+ " "+ deviceHardwareAddress);
                foundDevices.add(recev_device);
            }
        }
    };
}