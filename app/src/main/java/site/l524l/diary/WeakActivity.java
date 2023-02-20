package site.l524l.diary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.gson.Gson;

import site.l524l.diary.lesson.Weak;

public class WeakActivity extends AppCompatActivity {

    private static final String DAY_OF_WEAK = "day_of_weak";
    private static final String FAVORITES_WEAK = "{\"dayList\":[{\"dayOfWeak\":\"MONDAY\",\"lessons\":[{\"name\":\"Английский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Русский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Химия\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"География\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Биология\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"ОБЖ\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"TUESDAY\",\"lessons\":[{\"name\":\"Английский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Русский язык\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Литература\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10}]},{\"dayOfWeak\":\"WEDNESDAY\",\"lessons\":[{\"name\":\"Физика\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Математика (Э/к)\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"История\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Русский язык (Э/к)\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"THURSDAY\",\"lessons\":[{\"name\":\"История\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Английский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Литература\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Обществознание\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10}]},{\"dayOfWeak\":\"FRIDAY\",\"lessons\":[{\"name\":\"Родной язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Обществознание\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Русский язык\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Литература\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Информатика\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Инд.пр\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]}]}";
    private static final String MAIN_WEAK = "{\"dayList\":[{\"dayOfWeak\":\"MONDAY\",\"lessons\":[{\"name\":\"Чтение\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Английский язык\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20}]},{\"dayOfWeak\":\"TUESDAY\",\"lessons\":[{\"name\":\"Русский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Английский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Окружающий мир\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Родной язык\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10}]},{\"dayOfWeak\":\"WEDNESDAY\",\"lessons\":[{\"name\":\"Чтение\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Русский язык\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Изо\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10}]},{\"dayOfWeak\":\"THURSDAY\",\"lessons\":[{\"name\":\"Математика\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Русский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Музыка\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Окружающий мир\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Технология\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10}]},{\"dayOfWeak\":\"FRIDAY\",\"lessons\":[{\"name\":\"Чтение\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Русский язык\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"ОРКСЭ\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20}]}]}";
    private Weak weak;
    boolean isMain = true;

    private final Gson gson = new Gson();


    public WeakActivity() {}


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weak);
        boolean isMain = getIntent().getExtras().getBoolean("state");

        weak = isMain ? gson.fromJson(MAIN_WEAK, Weak.class) : gson.fromJson(FAVORITES_WEAK, Weak.class);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = findViewById(R.id.switch1);
        s.setChecked(!isMain);
        for (int i = 0; i < 5; i++) {
            LinearLayout layout = findViewById(R.id.daylist);
            FragmentContainerView container = new FragmentContainerView(this);
            container.setId(1001000+i);
            layout.addView(container);
            Bundle bundle = new Bundle();
            bundle.putSerializable(DAY_OF_WEAK, weak.getDayList().get(i));
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(container.getId(), DayFragment.class, bundle)
                        .commit();
            }
        }
    }

    public void toggleClass(View view){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent().putExtra("state",!getIntent().getExtras().getBoolean("state")));
        overridePendingTransition(0, 0);
    }

    public void goToMain(View view){
        onBackPressed();
    }
}