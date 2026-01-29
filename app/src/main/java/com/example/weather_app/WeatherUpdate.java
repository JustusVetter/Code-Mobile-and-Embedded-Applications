package com.example.weather_app;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherUpdate {
    private final TextView city, temperatureValue, windValue, humidityValue, cloudValue;
    private final ImageView currentWeatherImage;

    private final TextView[] dayViews = new TextView[5];
    private final TextView[] maxViews = new TextView[5];
    private final TextView[] minViews = new TextView[5];
    private final ImageView[] imageViews = new ImageView[5];

    public WeatherUpdate(
            TextView city, TextView temperatureValue, TextView windValue, TextView humidityValue, TextView cloudValue,
            ImageView currentWeatherImage,
            TextView day1, TextView day2, TextView day3, TextView day4, TextView day5,
            TextView maxDay1, TextView maxDay2, TextView maxDay3, TextView maxDay4, TextView maxDay5,
            TextView minDay1, TextView minDay2, TextView minDay3, TextView minDay4, TextView minDay5,
            ImageView imageDay1, ImageView imageDay2, ImageView imageDay3, ImageView imageDay4, ImageView imageDay5) {

        this.city = city;
        this.temperatureValue = temperatureValue;
        this.windValue = windValue;
        this.humidityValue = humidityValue;
        this.cloudValue = cloudValue;
        this.currentWeatherImage = currentWeatherImage;

        dayViews[0] = day1; dayViews[1] = day2; dayViews[2] = day3; dayViews[3] = day4; dayViews[4] = day5;
        maxViews[0] = maxDay1; maxViews[1] = maxDay2; maxViews[2] = maxDay3; maxViews[3] = maxDay4; maxViews[4] = maxDay5;
        minViews[0] = minDay1; minViews[1] = minDay2; minViews[2] = minDay3; minViews[3] = minDay4; minViews[4] = minDay5;
        imageViews[0] = imageDay1; imageViews[1] = imageDay2; imageViews[2] = imageDay3; imageViews[3] = imageDay4; imageViews[4] = imageDay5;
    }

    public void updateCurrentWeather(String result) {
        if (result == null) return;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject clouds = jsonObject.getJSONObject("clouds");
            double temperature = main.getDouble("temp");
            double humidity = main.getDouble("humidity");
            double cloudPercentage = clouds.getDouble("all");
            double wind = jsonObject.getJSONObject("wind").getDouble("speed");
            String iconCode = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            loadIcon(currentWeatherImage, iconCode);
            city.setText(jsonObject.getString("name"));
            temperatureValue.setText(String.format("%.0f°", temperature));
            humidityValue.setText(String.format("%.0f%%", humidity));
            windValue.setText(String.format("%.0f km/h", wind));
            cloudValue.setText(String.format("%.0f%%", cloudPercentage));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateForecast(String result) {
        if (result == null) return;
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray list = jsonObject.getJSONArray("list");
            int[] dayIndices = {0, 8, 16, 24, 32};

            for (int day = 0; day < 5; day++) {
                JSONObject item = list.getJSONObject(dayIndices[day]);
                JSONObject main = item.getJSONObject("main");
                long dt = item.getLong("dt");
                String dateStr = new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date(dt * 1000));
                String iconCode = item.getJSONArray("weather").getJSONObject(0).getString("icon");

                updateForecastDay(day, dateStr, main, iconCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateForecastDay(int day, String dateStr, JSONObject main, String iconCode) {
        loadIcon(imageViews[day], iconCode);
        dayViews[day].setText(dateStr);
        try {
            maxViews[day].setText(String.format("%.0f°", main.getDouble("temp_max")));
            minViews[day].setText(String.format("%.0f°", main.getDouble("temp_min")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadIcon(ImageView imageView, String iconCode) {
        String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        Glide.with(imageView.getContext()).load(iconUrl).into(imageView);
    }
}
