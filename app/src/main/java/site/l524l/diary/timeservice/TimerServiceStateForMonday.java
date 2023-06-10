package site.l524l.diary.timeservice;

import java.util.List;

import site.l524l.diary.entity.Lesson;
import site.l524l.diary.entity.Weak;

public class TimerServiceStateForMonday implements TimerServiceState {
    @Override
    public String getScheduleFor() {
        return "понедельник";
    }

    @Override
    public List<Lesson> getSchedule(Weak weak) {
        return weak.getDayList().get(0).getLessons();
    }
}
