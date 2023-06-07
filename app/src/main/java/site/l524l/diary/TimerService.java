package site.l524l.diary;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import site.l524l.diary.lesson.Lesson;
import site.l524l.diary.lesson.Weak;

public class TimerService {

    private final Weak weak;

    private String endTime;
    private int currentLesson = -1;
    private String endTo = "";

    public TimerService(Weak weak) {
        this.weak = weak;
    }

    public String getScheduleFor(){
        LocalDateTime dateTime = LocalDateTime.now();
        if (dateTime.getDayOfWeek().getValue() > 5) {
            return "Расписание на понедельник";
        } else if (dateTime.toLocalTime().isAfter(LocalTime.of(15, 0))) {
            return "Расписание на завтра";
        } else {
            return "Расписание на сегодня";
        }
    }

    public String getEndTimeTo(){
        update();
        return endTo;
    }
    public String getEndTime(){
        update();
        return endTime;
    }
    public int getCurrentLessonNumber(){
        update();
        return currentLesson;
    }
    public List<Lesson> getSchedule(){
        LocalDateTime dateTime = LocalDateTime.now();
        if (dateTime.getDayOfWeek().getValue() > 5) {
            return weak.getDayList().get(0).getLessons();
        } else if (dateTime.toLocalTime().isAfter(LocalTime.of(15, 0))) {
            return  weak.getDayList().get(dateTime.getDayOfWeek().getValue()).getLessons();
        } else {
            return weak.getDayList().get(dateTime.getDayOfWeek().getValue()-1).getLessons();
        }
    }
    private void update(){
        LocalDateTime dateTime = LocalDateTime.now();
        List<Lesson> lessons = getSchedule();

        if (dateTime.getDayOfWeek().getValue() < 6) {
            for (int i = 0; i < lessons.size(); i++) {
                if (dateTime.toLocalTime().isAfter(lessons.get(i).getStartTime()) &&
                        dateTime.toLocalTime().isBefore(lessons.get(i).getEndTime())) {
                    currentLesson = i;
                    endTime = lessons.get(i).getEndTime().minusSeconds(dateTime.toLocalTime().toSecondOfDay()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    endTo = "До конца урока";
                }

                if(dateTime.toLocalTime().isAfter(lessons.get(i).getEndTime()) &&
                        dateTime.toLocalTime().isBefore(lessons.get(i).getEndTime().plusMinutes(lessons.get(i).getaBreak()))){
                    currentLesson = i+1;
                    endTime = lessons.get(i).getEndTime().plusMinutes(lessons.get(i).getaBreak()).minusSeconds(dateTime.toLocalTime().toSecondOfDay()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    endTo = "До конца перемены";
                }
            }
        }
    }
}
