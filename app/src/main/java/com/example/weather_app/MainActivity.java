package com.example.weather_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private TextView city, temperatureValue, windValue, humidityValue, cloudValue, day1, day2, day3, day4, day5, maxDay1, maxDay2, maxDay3, maxDay4, maxDay5, minDay1, minDay2, minDay3, minDay4, minDay5;
    private ImageView imageDay1, imageDay2, imageDay3, imageDay4, imageDay5, currentWeatherImage;

    private Button searchBtn;

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

                // Process first 5 days (8 items per day = indices 0,8,16,24,32)
                int[] dayIndices = {0, 8, 16, 24, 32};

                for (int day = 0; day < 5; day++) {
                    JSONObject item = list.getJSONObject(dayIndices[day]);
                    JSONObject main = item.getJSONObject("main");

                    // Get date (dt is Unix timestamp)
                    long dt = item.getLong("dt");
                    String dateStr = new java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault())
                            .format(new java.util.Date(dt * 1000));

                    // Get weather icon
                    String iconCode = item.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("icon");
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    // Update UI for this day
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
}