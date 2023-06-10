package site.l524l.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import site.l524l.diary.entity.Weak;
import site.l524l.diary.retrofit.ApiService;
import site.l524l.diary.storage.WeakFileStorage;

public class DayListActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private SharedPreferences mSettings;
    private static final String APP_PREFERENCES = "mysettings";


    public DayListActivity() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://l524l.site:8443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        if(getIntent().getExtras().getBoolean("isNoFirst",false)) {
            findViewById(R.id.welcomeTextView).setVisibility(View.GONE);
        } else {
            findViewById(R.id.themeSettingsGroop).setVisibility(View.GONE);
        }
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        RadioButton radioButton = null;
        switch (mSettings.getInt("theme",2)) {
            case 0:
                radioButton = findViewById(R.id.lightThemeRadioButton);
                break;
            case 1:
                radioButton = findViewById(R.id.nightThemeRadioButton);
                break;
            case 2:
                radioButton = findViewById(R.id.systemThemeRadioButton);
                break;
        }
        radioButton.setChecked(true);

        Spinner spinner = findViewById(R.id.classSpinner);
        spinner.setSelection(mSettings.getInt("curentClass",0));
    }

    public void save(View view) {
        ApiService userService = retrofit.create(ApiService.class);
        try {
            Spinner spinner = findViewById(R.id.classSpinner);
            userService.getWeak("kuybyshevskaya",spinner.getSelectedItem().toString()).enqueue(new Callback<Weak>() {
                @Override
                public void onResponse(Call<Weak> call, Response<Weak> response) {
                    if(response.isSuccessful()) {
                        try {
                            WeakFileStorage fileStorage = new WeakFileStorage(getFilesDir());
                            fileStorage.uploadWeak(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putInt("curentClass", spinner.getSelectedItemPosition());
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();
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