package site.l524l.diary.lesson;

import java.util.ArrayList;
import java.util.List;

public class Weak {

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
