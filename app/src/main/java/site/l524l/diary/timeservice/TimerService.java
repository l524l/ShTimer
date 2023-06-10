package site.l524l.diary.timeservice;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import site.l524l.diary.entity.Lesson;
import site.l524l.diary.entity.Weak;

public class TimerService {

    private final Weak weak;
    private TimerServiceState currentState;

    private final TimerServiceStateForToday stateForToday;
    private final TimerServiceStateForTomorrow stateForTomorrow;
    private final TimerServiceStateForMonday stateForMonday;

    private String endTime;
    private int currentLesson = -1;
    private String endTo = "";

    public TimerService(Weak weak) {
        this.weak = weak;
        stateForToday = new TimerServiceStateForToday();
        stateForTomorrow = new TimerServiceStateForTomorrow();
        stateForMonday = new TimerServiceStateForMonday();
    }

    public String getScheduleFor(){
       updateState();
       return currentState.getScheduleFor();
    }

    public String getEndTimeTo(){
        updateState();
        return endTo;
    }
    public String getEndTime(){
        updateState();
        return endTime;
    }
    public int getCurrentLessonNumber(){
        updateState();
        return currentLesson;
    }
    public List<Lesson> getSchedule(){
        updateState();
        return currentState.getSchedule(weak);
    }
    private void updateState(){
        LocalDateTime dateTime = LocalDateTime.now();
        List<Lesson> schedule = getSchedule();

        if (dateTime.getDayOfWeek().getValue() < 6) {
            for (int i = 0; i < schedule.size(); i++) {
                if (dateTime.toLocalTime().isAfter(schedule.get(i).getStartTime()) &&
                        dateTime.toLocalTime().isBefore(schedule.get(i).getEndTime())) {
                    currentLesson = i;
                    endTime = schedule.get(i).getEndTime().minusSeconds(dateTime.toLocalTime().toSecondOfDay()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    endTo = "До конца урока";
                }

                if(dateTime.toLocalTime().isAfter(schedule.get(i).getEndTime()) &&
                        dateTime.toLocalTime().isBefore(schedule.get(i).getEndTime().plusMinutes(schedule.get(i).getaBreak()))){
                    currentLesson = i+1;
                    endTime = schedule.get(i).getEndTime().plusMinutes(schedule.get(i).getaBreak()).minusSeconds(dateTime.toLocalTime().toSecondOfDay()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    endTo = "До конца перемены";
                }
            }
        }
        if (dateTime.getDayOfWeek().getValue() > 5) {
            currentState = stateForMonday;
        } else if (dateTime.toLocalTime().isAfter(LocalTime.of(15, 0))) {
            currentState = stateForTomorrow;
        } else {
            currentState = stateForToday;
        }
    }
}
