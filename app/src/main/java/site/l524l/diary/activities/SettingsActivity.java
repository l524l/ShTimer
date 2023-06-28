package site.l524l.diary.activities;

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
import site.l524l.diary.R;
import site.l524l.diary.entity.Weak;
import site.l524l.diary.retrofit.ApiService;
import site.l524l.diary.storage.WeakFileStorage;

public class SettingsActivity extends AppCompatActivity {

    private final Retrofit retrofit;
    private SharedPreferences appPreferences;
    private static final String APP_PREFERENCES = "mysettings";
    private static final String IS_FIRST_LAUNCH_PREFERENCES = "isFirstLaunch";


    public SettingsActivity() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://l524l.site:8443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        appPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        showWelcomeTextIfFirstLaunch();

        updateThemeRadioButton();
        updateSpinner();
    }

    public void save(View view) {
        ApiService userService = retrofit.create(ApiService.class);
        WeakFileStorage fileStorage = new WeakFileStorage(getFilesDir());
        Spinner spinner = findViewById(R.id.classSpinner);
        userService.getWeak("kuybyshevskaya",spinner.getSelectedItem().toString()).enqueue(new Callback<Weak>() {
            @Override
            public void onResponse(Call<Weak> call, Response<Weak> response) {
                if(response.isSuccessful()) {
                    try {
                        fileStorage.uploadWeak(response.body());
                        saveCurrentClassToPreferences(spinner.getSelectedItemPosition());
                        updateFirstLaunchPreferences();
                        goToMainActivity();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Weak> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, "Проверьте соединение с интернетом!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setDayTheme(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putInt("theme", 0);
        editor.apply();
    }
    public void setNightTheme(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putInt("theme", 1);
        editor.apply();
    }
    public void setSystemTheme(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putInt("theme", 2);
        editor.apply();
    }

    private int tapCount = 0;
    public void activatePersonMode(View view){
        tapCount++;
        if(tapCount == 5) {
            Toast.makeText(SettingsActivity.this, "Включён личный функционал)", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = appPreferences.edit();
            editor.putBoolean("isPersonMode", true);
            editor.apply();
        } else if (tapCount > 5) {
            Toast.makeText(SettingsActivity.this, "Уже не надо)", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void saveCurrentClassToPreferences(int classNumber){
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putInt("curentClass", classNumber);
        editor.apply();
    }

    private void updateThemeRadioButton(){
        RadioButton radioButton = null;
        switch (appPreferences.getInt("theme",2)) {
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
    }

    private void updateSpinner(){
        Spinner spinner = findViewById(R.id.classSpinner);
        spinner.setSelection(appPreferences.getInt("curentClass",0));
    }

    private void showWelcomeTextIfFirstLaunch(){
        if(getIntent().getExtras().getBoolean("isFirstLaunch",false)) {
            findViewById(R.id.themeSettingsGroop).setVisibility(View.GONE);
        } else {
            findViewById(R.id.welcomeTextView).setVisibility(View.GONE);
        }
    }
    private void updateFirstLaunchPreferences(){
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(IS_FIRST_LAUNCH_PREFERENCES, false);
        editor.apply();
    }
}