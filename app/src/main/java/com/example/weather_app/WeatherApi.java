package com.example.weather_app;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherApi {
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private static final String API_KEY = "f70ae9a649de52d2829873e86648c65d";

    public static enum WeatherType {
        CURRENT("weather", "current"),
        FORECAST("forecast", "forecast");

        public final String endpoint;
        public final String type;

        WeatherType(String endpoint, String type) {
            this.endpoint = endpoint;
            this.type = type;
        }
    }

    public WeatherApi(ExecutorService executorService) {
        this.executorService = executorService;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void fetchWeatherData(String city, WeatherType type, WeatherCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/" + type.endpoint + "?q=" + city +
                "&appid="+ API_KEY + "&units=metric";

        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    mainHandler.post(() -> callback.onWeatherData(result));
                }
            } catch (IOException e) {
                Log.e("WeatherApi", "Fetch failed: " + type, e);
            }
        });
    }

    public interface WeatherCallback {
        void onWeatherData(String json);

        default void onError(String error) {
            Log.e("WeatherApi", "Error: " + error);
        }
    }

}
