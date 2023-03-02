package site.l524l.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.gson.Gson;

import java.time.LocalDateTime;

import site.l524l.diary.lesson.Weak;

public class WeakActivity extends AppCompatActivity {

    private static final String DAY_OF_WEAK = "day_of_weak";
    private static final String LOCAL_DATE_TIME = "local_date_time";

    private String MAIN_WEAK;
    private Weak weak;

    private final Gson gson = new Gson();


    public WeakActivity() {}


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weak);

        MAIN_WEAK = getIntent().getExtras().getString("weak");
        weak = gson.fromJson(MAIN_WEAK, Weak.class);

        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusDays(localDateTime.getDayOfWeek().getValue() - 1);

        for (int i = 0; i < 5; i++) {
            LinearLayout layout = findViewById(R.id.daylist);
            FragmentContainerView container = new FragmentContainerView(this);
            container.setId(1001000+i);
            layout.addView(container);

            Bundle bundle = new Bundle();
            bundle.putSerializable(DAY_OF_WEAK, weak.getDayList().get(i));
            bundle.putSerializable(LOCAL_DATE_TIME, localDateTime.plusDays(i));
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(container.getId(), DayFragment.class, bundle)
                        .commit();
            }
        }
    }

    public void goToMain(View view){
        onBackPressed();
    }
    public void goToSettings(View view){
        Intent intent = new Intent(getApplicationContext(), DayListActivity.class);
        intent.putExtra("isNoFirst", true);
        startActivity(intent);
        finish();
    }
}