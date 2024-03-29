package site.l524l.diary.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import site.l524l.diary.entity.Update;
import site.l524l.diary.entity.Weak;

public interface ApiService {
    @GET("/shcool/{shcool}/weak{classNumber}.json")
    Call<Weak> getWeak(@Path("shcool") String shcool, @Path("classNumber") String classNumber);

    @GET("/shtimer/update.json")
    Call<Update> getUpdate();
}
