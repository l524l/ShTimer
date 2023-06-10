package site.l524l.diary.timeservice;

import java.util.List;

import site.l524l.diary.entity.Lesson;
import site.l524l.diary.entity.Weak;

public interface TimerServiceState {
    String getScheduleFor();
    List<Lesson> getSchedule(Weak weak);
}
