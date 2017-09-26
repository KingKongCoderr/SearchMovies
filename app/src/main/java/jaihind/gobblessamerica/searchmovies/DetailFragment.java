package jaihind.gobblessamerica.searchmovies;


import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import jaihind.gobblessamerica.searchmovies.Model.Result;
import jaihind.gobblessamerica.searchmovies.Model.Search;
import jaihind.gobblessamerica.searchmovies.Network.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements Callback<Search>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;

    private TextView mtitle,myear,mplot,mgenre,mMetascore,mimdbRating,mruntime,mdirector;
    private ImageView mimage;

    public NetworkManager manager;
    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
        manager=new NetworkManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_detail, container, false);

        mtitle=(TextView)view.findViewById(R.id.detailTitle_tv);
        mimage=(ImageView)view.findViewById(R.id.detailImage_iv);
        myear=(TextView)view.findViewById(R.id.detailYear_tv);
        mplot=(TextView)view.findViewById(R.id.detailPlot_tv);

        mruntime=(TextView)view.findViewById(R.id.runtime_tv);
        mdirector=(TextView)view.findViewById(R.id.director_tv);
        mgenre=(TextView)view.findViewById(R.id.genre_tv);
        mMetascore=(TextView)view.findViewById(R.id.metascore_tv);
        mimdbRating=(TextView)view.findViewById(R.id.imdb_tv);

        fetchData(mParam1);

        return view;
    }
    private void fetchData(String query) {
        String url="https://www.omdbapi.com/";
        Map<String, String> parameter_data= new HashMap<>();
        parameter_data.put("i",query);

        parameter_data.put("plot","full");
        parameter_data.put("r","json");
        Call<Search> api_call= manager.getMovieService().getMovie(parameter_data);
        api_call.enqueue(this);

    }


    @Override
    public void onResponse(Call<Search> call, Response<Search> response) {

        if (response.isSuccessful()){

            Search obj=response.body();
            mtitle.setText(obj.getTitle());
            Glide.with(this).load(obj.getPoster()).asBitmap().placeholder(R.drawable.loading)
                    .error(R.drawable.notfound).into(mimage);
            myear.setText("ReleaseDate: "+obj.getReleased());
            mgenre.setText("Genre: "+obj.getGenre());
            mMetascore.setText("Metascore: "+obj.getMetascore());
            mimdbRating.setText("ImdbRating: "+obj.getImdbRating());
            mdirector.setText("Director: "+obj.getDirector());
            mruntime.setText("Run time: "+obj.getRuntime());
            mplot.setText("Plot: \n"+obj.getPlot());



        }else {
            Log.d("Response failure",response.message()+"");
        }
    }

    @Override
    public void onFailure(Call<Search> call, Throwable t) {
        if(t instanceof UnknownHostException){

            Log.e("On Failure","No network"+t.getMessage());
        }else{
            if(t instanceof SocketTimeoutException){
                Log.e("on Failure","Timeout"+t.getMessage());
            }
            else{
                Log.e("on Failure","error"+t.getMessage());
            }

        }
    }
}
