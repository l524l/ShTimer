package site.l524l.diary.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import site.l524l.diary.R;

public class LockScreenActivity extends AppCompatActivity {
    private static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        appPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        TextView v = findViewById(R.id.lockTitleTextView);
        TextView v1 = findViewById(R.id.lockMessageTextView);

        v.setText(appPreferences.getString("lock_title",""));
        v1.setText(appPreferences.getString("lock_message",""));
    }
}