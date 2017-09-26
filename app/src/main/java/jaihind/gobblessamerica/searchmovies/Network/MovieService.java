package jaihind.gobblessamerica.searchmovies.Network;

import java.util.List;
import java.util.Map;


import jaihind.gobblessamerica.searchmovies.Model.Result;
import jaihind.gobblessamerica.searchmovies.Model.Search;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by nande on 1/31/2017.
 */

public interface MovieService {


   /* @GET()
    Call<List<Movie>> getMovies(@Query("s") String search,@Query("y") String year, @Query("plot") String plot, @Query("r") String response);*/

    @GET(".")
    Call<Result> getMovies(@QueryMap Map<String, String> parameters);

    @GET(".")
    Call<Search> getMovie(@QueryMap Map<String,String> parameters);
}
