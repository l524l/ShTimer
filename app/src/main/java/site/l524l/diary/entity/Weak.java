package site.l524l.diary.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Weak implements Serializable {

    private List<Day> dayList;

    public Weak() {
        dayList = new ArrayList<>();
    }


    public void addDay(Day day){
        dayList.add(day);
    }

    public List<Day> getDayList() {
        return dayList;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = dayList;
    }
}
