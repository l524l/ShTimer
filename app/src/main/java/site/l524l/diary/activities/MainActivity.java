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
import site.l524l.diary.TimerService;
import site.l524l.diary.entity.Lesson;
import site.l524l.diary.entity.Update;
import site.l524l.diary.entity.Weak;
import site.l524l.diary.retrofit.ApiService;
import site.l524l.diary.storage.WeakFileStorage;

public class MainActivity extends AppCompatActivity {

    private static final String IS_FAVORITE_PREFERENCES = "isFavorite";
    private static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    
    private Weak weak;
    private UpdateScreenTask catTask;
    private Retrofit retrofit;
    private WeakFileStorage fileStorage;

    public MainActivity() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://l524l.site:8443/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        catTask = new UpdateScreenTask();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileStorage = new WeakFileStorage(getFilesDir());
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        checkUpdate();
        updateTheme();

        if(fileStorage.isFileExist()){
            updateWeak();
            updateFavoriteSwitch();
        } else {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.putExtra("isNoFirst", false);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        catTask = new UpdateScreenTask();
        catTask.execute();

        checkUpdate();
        updateWeak();
        updateFavoriteSwitch();
    }

    @Override
    protected void onPause() {
        catTask.cancel(true);
        super.onPause();
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateScreenTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... parameter) {
            while (!isCancelled()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return 0;
                }
                publishProgress();
            }
            return null;
        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void onProgressUpdate(Integer... progress) {
            TimerService timerService = new TimerService(weak);
            TextView title = findViewById(R.id.timerTitleTextView);
            TextView timer = findViewById(R.id.timerTextView);
            TextView textView2 = findViewById(R.id.scheduleForTextView);

            title.setText("");
            timer.setText("");

            textView2.setText(timerService.getScheduleFor());

            title.setText("");
            timer.setText("");
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
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_FAVORITE_PREFERENCES, !mSettings.getBoolean(IS_FAVORITE_PREFERENCES,false));
        editor.apply();
        updateWeak();
        TimerService timerService = new TimerService(weak);

        updateDaySchedule(timerService.getSchedule(),timerService.getCurrentLessonNumber());
    }

    public void updateFavoriteSwitch() {
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch s = findViewById(R.id.favoritSwitch1);
        s.setChecked(mSettings.getBoolean(IS_FAVORITE_PREFERENCES,false));
        if (mSettings.getBoolean("isPersonMode",false)) {
            s.setVisibility(View.VISIBLE);
        }
    }

    public void updateWeak() {
        try {
            if (mSettings.getBoolean(IS_FAVORITE_PREFERENCES,false)) {
                weak = fileStorage.loadFavoriteWeak();
            }else {
                weak = fileStorage.loadWeak();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTheme(){
        switch (mSettings.getInt("theme",2)) {
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

    public void updateDaySchedule(List<Lesson> lessons, int currentLesson){
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

    public void checkUpdate(){
        ApiService userService = retrofit.create(ApiService.class);
        userService.getUpdate().enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if(response.isSuccessful()) {
                    Update update = response.body();


                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putBoolean("lock_status", update.LOCK_STATUS);
                    editor.putString("lock_title", update.LOCK_TITLE);
                    editor.putString("lock_message", update.LOCK_MESSAGE);
                    editor.putString("version_status", update.VERSION_STATUS);
                    editor.putString("update_link", update.UPDATE_LINK);

                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Не удалось проверить наличие обновлений", Toast.LENGTH_SHORT).show();
            }
        });
        if (mSettings.getBoolean("lock_status",false)) {
            Intent intent = new Intent(getApplicationContext(), LockScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        } else if (!mSettings.getString("version_status", getResources().getString(R.string.app_version)).equals(getResources().getString(R.string.app_version))) {
            Intent intent = new Intent(getApplicationContext(), UpdateScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        }
    }
}