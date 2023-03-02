package site.l524l.diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import site.l524l.diary.lesson.Lesson;
import site.l524l.diary.lesson.Weak;

public class MainActivity extends AppCompatActivity {

    private String MAIN_WEAK;
    private static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;
    
    private Weak weak;
    private final Gson gson = new Gson();
    private MyAsyncTask catTask;
    
    
    public MainActivity() {
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File internalStorageDir = getFilesDir();
        File file = new File(internalStorageDir,"weak.json");
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

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

        if(file.exists()){
            try {
                byte[] raw = Files.readAllBytes(file.toPath());
                MAIN_WEAK = new String(raw,"utf-8");
                weak = gson.fromJson(MAIN_WEAK, Weak.class);
                Files.deleteIfExists(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), DayListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        catTask = new MyAsyncTask();
        catTask.execute();
    }

    @Override
    protected void onPause() {
        catTask.cancel(true);
        super.onPause();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... parameter) {
            while (!isCancelled()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return 0;
                }

                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            TextView title = findViewById(R.id.t1);
            TextView timer = findViewById(R.id.timer);
            TextView textView2 = findViewById(R.id.textView2);

            LocalDateTime dateTime = LocalDateTime.now();
            Group group = findViewById(R.id.labels_group);
            int[] ids = group.getReferencedIds();

            title.setText("");
            timer.setText("");

            List<Lesson> lessons;

            if (dateTime.getDayOfWeek().getValue() > 5) {
                lessons = weak.getDayList().get(0).getLessons();
                textView2.setText(getResources().getString(R.string.schedule_monday));
            } else if (dateTime.toLocalTime().isAfter(LocalTime.of(15, 0))) {
                lessons = weak.getDayList().get(dateTime.getDayOfWeek().getValue()).getLessons();
                textView2.setText(getResources().getString(R.string.schedule_tomorrow));
            } else {
                lessons = weak.getDayList().get(dateTime.getDayOfWeek().getValue()-1).getLessons();
                textView2.setText(getResources().getString(R.string.schedule_today));
            }

            if (lessons == null) return;

            int currentLesson = -1;
            if (dateTime.getDayOfWeek().getValue() < 6) {
                for (int i = 0; i < lessons.size(); i++) {
                    if (dateTime.toLocalTime().isAfter(lessons.get(i).getStartTime()) &&
                            dateTime.toLocalTime().isBefore(lessons.get(i).getEndTime())) {
                        timer.setText(lessons.get(i).getEndTime().minusSeconds(dateTime.toLocalTime().toSecondOfDay()).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        title.setText(getResources().getString(R.string.edn_to_lesson));

                        currentLesson = i;
                    }

                    if(dateTime.toLocalTime().isAfter(lessons.get(i).getEndTime()) &&
                            dateTime.toLocalTime().isBefore(lessons.get(i).getEndTime().plusMinutes(lessons.get(i).getaBreak()))){
                        timer.setText(lessons.get(i).getEndTime().plusMinutes(lessons.get(i).getaBreak()).minusSeconds(dateTime.toLocalTime().toSecondOfDay()).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                        title.setText(getResources().getString(R.string.end_to_break));
                        currentLesson = i+1;
                    }
                }
            }
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
    }

    public void goToTimetable(View view){
        Intent intent = new Intent(getApplicationContext(), WeakActivity.class);
        intent.putExtra("weak", MAIN_WEAK);
        startActivity(intent);
    }
}