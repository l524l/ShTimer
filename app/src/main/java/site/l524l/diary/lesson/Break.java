package site.l524l.diary.lesson;

import java.io.Serializable;
import java.time.LocalTime;

public class Break implements Serializable {

    private String startTime;
    private String endTime;

    public LocalTime getStartTime() {
        return LocalTime.parse(startTime);
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime.toString();
    }

    public LocalTime getEndTime() {
        return LocalTime.parse(endTime);
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime.toString();
    }
}
