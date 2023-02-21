package site.l524l.diary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.google.gson.Gson;

import site.l524l.diary.lesson.Weak;

public class WeakActivity extends AppCompatActivity {

    private static final String DAY_OF_WEAK = "day_of_weak";

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

    public void goToMain(View view){
        onBackPressed();
    }
}