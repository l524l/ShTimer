package site.l524l.diary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LockScreen extends AppCompatActivity {
    private static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        TextView v = findViewById(R.id.lockTitleTextView);
        TextView v1 = findViewById(R.id.lockMessageTextView);

        v.setText(mSettings.getString("lock_title",""));
        v1.setText(mSettings.getString("lock_message",""));
    }
}