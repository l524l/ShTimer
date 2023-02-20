package site.l524l.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import site.l524l.diary.lesson.Lesson;
import site.l524l.diary.lesson.Weak;

public class MainActivity extends AppCompatActivity {

    private static final String FAVORITES_WEAK = "{\"dayList\":[{\"dayOfWeak\":\"MONDAY\",\"lessons\":[{\"name\":\"Английский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Русский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Химия\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"География\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Биология\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"ОБЖ\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"TUESDAY\",\"lessons\":[{\"name\":\"Английский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Русский язык\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Литература\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10}]},{\"dayOfWeak\":\"WEDNESDAY\",\"lessons\":[{\"name\":\"Физика\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Математика (Э/к)\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"История\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Русский язык (Э/к)\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"THURSDAY\",\"lessons\":[{\"name\":\"История\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Английский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Литература\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Обществознание\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10}]},{\"dayOfWeak\":\"FRIDAY\",\"lessons\":[{\"name\":\"Родной язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Обществознание\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Русский язык\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Литература\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Информатика\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Инд.пр\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]}]}";
    private String MAIN_WEAK;
    boolean isMain = true;
    
    private Weak weak;
    private final Gson gson = new Gson();
    private MyAsyncTask catTask;
    
    
    public MainActivity() {
        //weak = gson.fromJson(MAIN_WEAK, Weak.class);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File internalStorageDir = getFilesDir();
        File file = new File(internalStorageDir,"weak.json");
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
            intent.putExtra("state", isMain);
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

        if (isMain) {
            @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = findViewById(R.id.switch2);
            s.setChecked(false);
        }
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
            textView2.setText("");

            for (int id :
                    ids) {
                TextView textView = findViewById(id);
                textView.setText(getResources().getString(R.string.presholder));
            }

            List<Lesson> lessons = null;

            for (int i = 0; i < weak.getDayList().size(); i++) {
                if (dateTime.toLocalTime().isAfter(LocalTime.of(15, 0))) {
                    if (weak.getDayList().get(i).getDayOfWeak().equals(dateTime.plusDays(1).getDayOfWeek())) lessons = weak.getDayList().get(i).getLessons();
                    textView2.setText(getResources().getString(R.string.schedule_tomorrow));
                }
                if (weak.getDayList().get(i).getDayOfWeak().equals(dateTime.getDayOfWeek())) {
                    lessons = weak.getDayList().get(i).getLessons();
                    textView2.setText(getResources().getString(R.string.schedule_today));
                }
            }

            if (lessons == null) return;

            int currentLesson = -1;

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
    
    public void toggleClass(View view){
        if (isMain){
            weak = gson.fromJson(FAVORITES_WEAK, Weak.class);
        } else {
            weak = gson.fromJson(MAIN_WEAK, Weak.class);
        }
        isMain = !isMain;
    }

    public void goToTimetable(View view){
        Intent intent = new Intent(getApplicationContext(), WeakActivity.class);
        intent.putExtra("state", isMain);
        startActivity(intent);
    }
}