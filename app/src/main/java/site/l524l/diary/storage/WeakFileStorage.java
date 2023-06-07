package site.l524l.diary.storage;

import com.google.gson.Gson;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import site.l524l.diary.lesson.Weak;

public class WeakFileStorage {
    private static final String FAVORITE_WEAK = "{\"dayList\":[{\"dayOfWeak\":\"MONDAY\",\"lessons\":[{\"name\":\"Английский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Русский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Химия\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"География\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Биология\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"ОБЖ\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"TUESDAY\",\"lessons\":[{\"name\":\"Английский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Русский язык\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Литература\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"-\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"WEDNESDAY\",\"lessons\":[{\"name\":\"Физика\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Математика (Э/к)\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"История\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Русский язык (Э/к)\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"THURSDAY\",\"lessons\":[{\"name\":\"История\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Английский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Литература\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Обществознание\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"-\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"FRIDAY\",\"lessons\":[{\"name\":\"Родной язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Обществознание\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Русский язык\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Литература\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Информатика\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Инд.пр\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]}]}";
    // 10 private static final String FAVORITE_WEAK = "{\"dayList\":[{\"dayOfWeak\":\"MONDAY\",\"lessons\":[{\"name\":\"Математика\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Биология\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Русский язык\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"История\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Английский язык\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"TUESDAY\",\"lessons\":[{\"name\":\"Физика\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Физ-ра\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Обществознание\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Литература\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Математика\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Английский язык\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"WEDNESDAY\",\"lessons\":[{\"name\":\"Физ-ра\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Русский язык\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Литература\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Химия\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Родной язык\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Математика (Э/к)\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"-\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"THURSDAY\",\"lessons\":[{\"name\":\"Русский язык\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Информатика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"История\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Английский язык\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"Русский язык (Э/к)\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"Астрономия\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Физ-ра\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]},{\"dayOfWeak\":\"FRIDAY\",\"lessons\":[{\"name\":\"Литература\",\"startTime\":\"08:30\",\"endTime\":\"09:10\",\"aBreak\":15},{\"name\":\"Математика\",\"startTime\":\"09:25\",\"endTime\":\"10:05\",\"aBreak\":10},{\"name\":\"Обществознание\",\"startTime\":\"10:15\",\"endTime\":\"10:55\",\"aBreak\":10},{\"name\":\"Физика\",\"startTime\":\"11:05\",\"endTime\":\"11:45\",\"aBreak\":20},{\"name\":\"География\",\"startTime\":\"12:05\",\"endTime\":\"12:45\",\"aBreak\":10},{\"name\":\"ОБЖ\",\"startTime\":\"12:55\",\"endTime\":\"13:35\",\"aBreak\":10},{\"name\":\"Инд.пр\",\"startTime\":\"13:45\",\"endTime\":\"14:25\",\"aBreak\":20}]}]}";

    private final File weakFile;
    private final Gson gson = new Gson();

    public WeakFileStorage(File filesDir) {
        this.weakFile = new File(filesDir,"weak.json");
    }

    public Weak loadFavoriteWeak(){
        return gson.fromJson(FAVORITE_WEAK, Weak.class);
    }
    public Weak loadWeak() throws Exception {
        if (isFileExist()){
            byte[] raw = Files.readAllBytes(weakFile.toPath());
            String weak = new String(raw,"utf-8");
            return gson.fromJson(weak, Weak.class);
        } else throw new Exception();
    }
    public void uploadWeak(String weak) throws Exception {
        Files.write(weakFile.toPath(),weak.getBytes(StandardCharsets.UTF_8));
    }
    public void uploadWeak(Weak weak) throws Exception {
        uploadWeak(gson.toJson(weak));
    }
    public boolean isFileExist() {
        return weakFile.exists();
    }
}
