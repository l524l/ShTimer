package site.l524l.diary.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import site.l524l.diary.R;
import site.l524l.diary.entity.Day;
import site.l524l.diary.entity.Lesson;

public class DayScheduleFragment extends Fragment {

    private static final String DAY_OF_WEAK = "day_of_weak";
    private static final String LOCAL_DATE_TIME = "local_date_time";
    private Day day;
    private LocalDateTime localDateTime;


    public DayScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = (Day) getArguments().getSerializable(DAY_OF_WEAK);
            localDateTime = (LocalDateTime) getArguments().getSerializable(LOCAL_DATE_TIME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_schedule, container, false);
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView z = view.findViewById(R.id.dayOfWeakTextView);
        TextView date = view.findViewById(R.id.dateTextView);
        Group labelsGroup = view.findViewById(R.id.leasons_labels);
        int[] ids = labelsGroup.getReferencedIds();
        List<Lesson> lessons = day.getLessons();

        z.setText(day.getDayOfWeak().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")).toUpperCase(Locale.ROOT));
        date.setText(String.format("%s %s", localDateTime.getDayOfMonth(),localDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))));
        for (int i = 0; i < lessons.size(); i++) {
            TextView textView = view.findViewById(ids[i]);
            textView.setText(String.format(getResources().getString(R.string.lesson_format),i+1, lessons.get(i).getName()));
        }

        super.onViewCreated(view, savedInstanceState);
    }
}