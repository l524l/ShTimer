package site.l524l.diary.entity;

import java.io.Serializable;

public class Update implements Serializable {
    public boolean LOCK_STATUS;
    public String LOCK_TITLE;
    public String LOCK_MESSAGE;

    public String VERSION_STATUS;
    public String UPDATE_LINK;
}
