package site.l524l.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import site.l524l.diary.lesson.Weak;
import site.l524l.diary.retrofit.ApiService;

public class DayListActivity extends AppCompatActivity {
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://l524l.site:8443/shcool/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void save(View view) {
        Weak weak = null;
        ApiService userService = retrofit.create(ApiService.class);
        try {
            Spinner spinner = findViewById(R.id.spinner);
            userService.getWeak("kuybyshevskaya",spinner.getSelectedItem().toString()).enqueue(new Callback<Weak>() {
                @Override
                public void onResponse(Call<Weak> call, Response<Weak> response) {
                    response.body();
                    File internalStorageDir = getFilesDir();
                    File file = new File(internalStorageDir,"weak.json");
                    Gson gson = new Gson();
                    String s = gson.toJson(response.body());
                    try {
                        Files.write(file.toPath(),s.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Weak> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}