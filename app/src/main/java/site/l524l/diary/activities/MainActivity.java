package site.l524l.diary.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.Group;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import site.l524l.diary.R;
import site.l524l.diary.timeservice.TimerService;
import site.l524l.diary.entity.Lesson;
import site.l524l.diary.entity.Update;
import site.l524l.diary.entity.Weak;
import site.l524l.diary.retrofit.ApiService;
import site.l524l.diary.storage.WeakFileStorage;

public class MainActivity extends AppCompatActivity {

    private static final String IS_FAVORITE_PREFERENCES = "isFavorite";
    private static final String IS_FIRST_LAUNCH_PREFERENCES = "isFirstLaunch";
    private static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences appPreferences;
    
    private Weak weak;
    private UpdateScreenTask updateScreenTask;
    private final Retrofit retrofit;
    private WeakFileStorage fileStorage;

    public MainActivity() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://l524l.site:8443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        updateScreenTask = new UpdateScreenTask();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileStorage = new WeakFileStorage(getFilesDir());
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        appPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        checkUpdate();
        updateTheme();

        if(isFirstLaunch()){
            goToSettingsActivityWhenFirstLaunch();
            finish();
        } else {
            updateWeak();
            updateFavoriteSwitch();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateScreenTask = new UpdateScreenTask();
        updateScreenTask.execute();

        checkUpdate();
        updateWeak();
        updateFavoriteSwitch();
    }

    @Override
    protected void onPause() {
        updateScreenTask.cancel(true);
        super.onPause();
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateScreenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... parameter) {
            while (!isCancelled()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
                publishProgress();
            }
            return null;
        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void onProgressUpdate(Void... progress) {
            TimerService timerService = new TimerService(weak);
            TextView title = findViewById(R.id.timerTitleTextView);
            TextView timer = findViewById(R.id.timerTextView);
            TextView scheduleForTextView = findViewById(R.id.scheduleForTextView);

            title.setText("");
            timer.setText("");

            scheduleForTextView.setText(timerService.getScheduleFor());
            timer.setText(timerService.getEndTime());
            title.setText(timerService.getEndTimeTo());

            updateDaySchedule(timerService.getSchedule(),timerService.getCurrentLessonNumber());
        }
    }

    public void goToTimetable(View view){
        Intent intent = new Intent(getApplicationContext(), WeekScheduleActivity.class);
        startActivity(intent);
    }

    public void toggleWeak(View view){
        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(IS_FAVORITE_PREFERENCES, !appPreferences.getBoolean(IS_FAVORITE_PREFERENCES,false));
        editor.apply();
        updateWeak();
        TimerService timerService = new TimerService(weak);

        updateDaySchedule(timerService.getSchedule(),timerService.getCurrentLessonNumber());
    }

    private void updateFavoriteSwitch() {
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch s = findViewById(R.id.favoritSwitch1);
        s.setChecked(appPreferences.getBoolean(IS_FAVORITE_PREFERENCES,false));
        if (appPreferences.getBoolean("isPersonMode",false)) {
            s.setVisibility(View.VISIBLE);
        }
    }

    private void updateWeak() {
        try {
            if (appPreferences.getBoolean(IS_FAVORITE_PREFERENCES,false)) {
                weak = fileStorage.loadFavoriteWeak();
            }else {
                weak = fileStorage.loadWeak();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTheme(){
        switch (appPreferences.getInt("theme",2)) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    private void updateDaySchedule(List<Lesson> lessons, int currentLesson){
        Group group = findViewById(R.id.labels_group);
        int[] ids = group.getReferencedIds();
        for (int i = 0; i < lessons.size(); i++) {
            TextView textView = findViewById(ids[i]);
            textView.setText(lessons.get(i).getName());
            textView.setTextSize(16);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            if (i == currentLesson) {
                textView.setText(String.format(getResources().getString(R.string.current_lesson_format), lessons.get(i).getName()));
                textView.setTextSize(18);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }
    }

    private void checkUpdate(){
        ApiService userService = retrofit.create(ApiService.class);
        userService.getUpdate().enqueue(new Callback<Update>() {
            @Override
            public void onResponse(@NonNull Call<Update> call, @NonNull Response<Update> response) {
                if(response.isSuccessful()) {
                    Update update = response.body();


                    SharedPreferences.Editor editor = appPreferences.edit();
                    editor.putBoolean("lock_status", update.LOCK_STATUS);
                    editor.putString("lock_title", update.LOCK_TITLE);
                    editor.putString("lock_message", update.LOCK_MESSAGE);
                    editor.putString("version_status", update.VERSION_STATUS);
                    editor.putString("update_link", update.UPDATE_LINK);

                    editor.apply();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Update> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Не удалось проверить наличие обновлений", Toast.LENGTH_SHORT).show();
            }
        });
        if (appPreferences.getBoolean("lock_status",false)) {
            goToLockActivity();
            finish();
        } else if (!appPreferences.getString("version_status", getResources().getString(R.string.app_version)).equals(getResources().getString(R.string.app_version))) {
            goToUpdateActivity();
            finish();
        }
    }
    private void goToSettingsActivityWhenFirstLaunch(){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.putExtra("isFirstLaunch", true);
        startActivity(intent);
    }
    private void goToUpdateActivity(){
        Intent intent = new Intent(getApplicationContext(), UpdateScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
    private void goToLockActivity(){
        Intent intent = new Intent(getApplicationContext(), UpdateScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    private boolean isFirstLaunch(){
        return appPreferences.getBoolean(IS_FIRST_LAUNCH_PREFERENCES,true);
    }
}