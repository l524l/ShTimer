package site.l524l.diary.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import site.l524l.diary.lesson.Weak;

public interface ApiService {
    @GET("{shcool}/weak{classNumber}.json")
    Call<Weak> getWeak(@Path("shcool") String shcool, @Path("classNumber") String classNumber);
}
