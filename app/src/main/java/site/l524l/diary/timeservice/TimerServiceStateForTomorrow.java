package site.l524l.diary.timeservice;

import java.time.LocalDateTime;
import java.util.List;

import site.l524l.diary.entity.Lesson;
import site.l524l.diary.entity.Weak;

public class TimerServiceStateForTomorrow implements TimerServiceState{
    @Override
    public String getScheduleFor() {
        return "завтра";
    }

    @Override
    public List<Lesson> getSchedule(Weak weak) {
        LocalDateTime dateTime = LocalDateTime.now();
        return weak.getDayList().get(dateTime.getDayOfWeek().getValue()).getLessons();
    }
}
