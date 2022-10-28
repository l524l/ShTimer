package site.l524l.diary.lesson;

import java.time.LocalTime;

public class Lesson {

    private String name;
    private String startTime;
    private String endTime;
    private int aBreak;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getaBreak() {
        return aBreak;
    }

    public void setaBreak(int aBreak) {
        this.aBreak = aBreak;
    }
}
