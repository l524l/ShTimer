package site.l524l.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
    private SharedPreferences mSettings;
    private static final String APP_PREFERENCES = "mysettings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        if(getIntent().getExtras().getBoolean("isNoFirst",false)) {
            findViewById(R.id.textView).setVisibility(View.GONE);
        } else {
            findViewById(R.id.radio_groop).setVisibility(View.GONE);
        }
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        RadioButton radioButton = null;
        switch (mSettings.getInt("theme",2)) {
            case 0:
                radioButton = findViewById(R.id.light);
                break;
            case 1:
                radioButton = findViewById(R.id.night);
                break;
            case 2:
                radioButton = findViewById(R.id.system);
                break;
        }
        radioButton.setChecked(true);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setSelection(mSettings.getInt("curentClass",0));
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
                    if(response.isSuccessful()) {
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
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putInt("curentClass", spinner.getSelectedItemPosition());
                        editor.apply();

                        finish();
                    } else {
                        Toast.makeText(DayListActivity.this, "Ошибка сервера!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Weak> call, Throwable t) {
                    Toast.makeText(DayListActivity.this, "Проверьте соединение с интернетом!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setDayTheme(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("theme", 0);
        editor.apply();
    }
    public void setNightTheme(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("theme", 1);
        editor.apply();
    }
    public void setSystemTheme(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("theme", 2);
        editor.apply();
    }

    private int count = 0;
    public void personMode(View view){
        count++;
        if(count == 5) {
            Toast.makeText(DayListActivity.this, "Включён личный функционал)", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("isPersonMode", true);
            editor.apply();
        } else if (count > 5) {
            Toast.makeText(DayListActivity.this, "Уже не надо)", Toast.LENGTH_SHORT).show();
        }
    }
}