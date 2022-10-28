package site.l524l.diary;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import site.l524l.diary.lesson.Day;
import site.l524l.diary.lesson.Lesson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DAY_OF_WEAK = "day_of_weak";

    // TODO: Rename and change types of parameters
    private Day day;


    public static BlankFragment newInstance(Day param1) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putSerializable(DAY_OF_WEAK, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = (Day) getArguments().getSerializable(DAY_OF_WEAK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView z = view.findViewById(R.id.textView11);
        Group labelsGroup = view.findViewById(R.id.leasons_labels);
        int[] ids = labelsGroup.getReferencedIds();
        List<Lesson> lessons = day.getLessons();

        z.setText(day.getDayOfWeak().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")).toUpperCase(Locale.ROOT));

        for (int i = 0; i < lessons.size(); i++) {
            TextView textView = view.findViewById(ids[i]);
            textView.setText(lessons.get(i).getName());
        }

        super.onViewCreated(view, savedInstanceState);
    }
}