package site.l524l.diary.lesson;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Day implements Serializable {

    private DayOfWeek dayOfWeak;

    private List<Lesson> lessons;


    public Day() {
        lessons = new ArrayList();
    }


    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }


    public DayOfWeek getDayOfWeak() {
        return dayOfWeak;
    }

    public void setDayOfWeak(DayOfWeek dayOfWeak) {
        this.dayOfWeak = dayOfWeak;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
