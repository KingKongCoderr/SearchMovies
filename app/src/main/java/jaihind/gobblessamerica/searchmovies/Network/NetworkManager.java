package jaihind.gobblessamerica.searchmovies.Network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nande on 1/31/2017.
 */

public class NetworkManager {
    public String Base_url="https://www.omdbapi.com/";
    public MovieService movieService;
    public Retrofit mretrobuilder;
    public NetworkManager(){
        mretrobuilder= new Retrofit.Builder().baseUrl(Base_url).addConverterFactory(GsonConverterFactory.create()).build();
        movieService= mretrobuilder.create(MovieService.class);
    }

    public MovieService getMovieService() {
        return movieService;
    }


}
