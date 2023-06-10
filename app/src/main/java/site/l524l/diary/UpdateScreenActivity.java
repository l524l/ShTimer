package site.l524l.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateScreenActivity extends AppCompatActivity {

    private static final String APP_PREFERENCES = "mysettings";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_screen);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        TextView t = findViewById(R.id.versionTextView);

        t.setText(String.format("ShTimer %s",mSettings.getString("version_status","")));
    }

    public void link(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mSettings.getString("update_link","")));
        startActivity(browserIntent);
    }
}