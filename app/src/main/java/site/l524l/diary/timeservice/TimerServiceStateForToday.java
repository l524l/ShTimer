package site.l524l.diary.timeservice;

import java.time.LocalDateTime;
import java.util.List;

import site.l524l.diary.entity.Lesson;
import site.l524l.diary.entity.Weak;

public class TimerServiceStateForToday implements TimerServiceState{
    @Override
    public String getScheduleFor() {
        return "сегодня";
    }

    @Override
    public List<Lesson> getSchedule(Weak weak) {
        LocalDateTime dateTime = LocalDateTime.now();
        return weak.getDayList().get(dateTime.getDayOfWeek().getValue()-1).getLessons();
    }
}
