package site.l524l.diary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import java.time.LocalDateTime;

import site.l524l.diary.entity.Weak;
import site.l524l.diary.storage.WeakFileStorage;

public class WeakActivity extends AppCompatActivity {

    private static final String DAY_OF_WEAK = "day_of_weak";
    private static final String LOCAL_DATE_TIME = "local_date_time";
    private static final String APP_PREFERENCES = "mysettings";
    private static final String IS_FAVORITE_PREFERENCES = "isFavorite";
    private SharedPreferences mSettings;
    private WeakFileStorage fileStorage;
    private Weak weak;


    public WeakActivity() {

    }


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weak);
        fileStorage = new WeakFileStorage(getFilesDir());
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch s = findViewById(R.id.favoritSwitch2);

        if (mSettings.getBoolean("isPersonMode",false)) {
            s.setVisibility(View.VISIBLE);
        }

        if (mSettings.getBoolean(IS_FAVORITE_PREFERENCES,false)){
            weak = fileStorage.loadFavoriteWeak();
            s.setChecked(true);
        } else {
            try {
                weak = fileStorage.loadWeak();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusDays(localDateTime.getDayOfWeek().getValue() - 1);

        for (int i = 0; i < 5; i++) {
            LinearLayout layout = findViewById(R.id.scheduleLinerLayout);
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
    public void toggleClass(View view){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(IS_FAVORITE_PREFERENCES, !mSettings.getBoolean(IS_FAVORITE_PREFERENCES,false));
        editor.apply();

        startActivity(getIntent());
        overridePendingTransition(0, 0);
        finish();
    }
}