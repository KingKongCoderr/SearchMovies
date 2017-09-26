package jaihind.gobblessamerica.searchmovies;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import jaihind.gobblessamerica.searchmovies.Model.Result;
import jaihind.gobblessamerica.searchmovies.Model.Search;
import jaihind.gobblessamerica.searchmovies.Network.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements Callback<Result> {

    public movieSelectedListener mlistener;

    public interface movieSelectedListener{
        public void onMovieSelected(String imdb_id);
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String Query="";
    public NetworkManager mnetworkManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecyclerView mrecyclerview;
    public RecyclerView.LayoutManager mlayoutmanager;
    public SharedPreferences msharedpreferences;
    public SharedPreferences.Editor msp_editor;
    public MovieAdapter madapter;



    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try{

            mlistener=(movieSelectedListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+"must implement onMovieSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mnetworkManager=new NetworkManager();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_list, container, false);

        msharedpreferences= getActivity().getPreferences(Context.MODE_PRIVATE);
        msp_editor= msharedpreferences.edit();
        Query= msharedpreferences.getString("query","movie");

        mrecyclerview=(RecyclerView)view.findViewById(R.id.recycler_view);
        if (mrecyclerview != null) {
            mrecyclerview.setHasFixedSize(true);
        }
        mlayoutmanager=new LinearLayoutManager(getContext());
       // mlayoutmanager=new GridLayoutManager(getContext(),2);
        mrecyclerview.setLayoutManager(mlayoutmanager);

        fetchData(Query);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem item= menu.findItem(R.id.search);
        SearchView searchview= (SearchView) item.getActionView();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("hello",query);


                msp_editor.putString("query",query);
                msp_editor.commit();
                fetchData(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



    }

    private void fetchData(String query) {
        String url="https://www.omdbapi.com/";
        Map<String, String> parameter_data= new HashMap<>();
        parameter_data.put("s",query);
        parameter_data.put("y","");
        parameter_data.put("plot","short");
        parameter_data.put("r","json");
        Call<Result> api_call= mnetworkManager.getMovieService().getMovies(parameter_data);
        api_call.enqueue(this);

    }


    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {

        if (response.isSuccessful()){

            Result obj=response.body();
            List<Search> movies_list= obj.getSearch();

            String res= obj.getResponse();
            if (movies_list!=null){
            madapter=new MovieAdapter(movies_list,getActivity(),this);
            mrecyclerview.setAdapter(madapter);
                madapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getContext(), "Movie Not found", Toast.LENGTH_SHORT).show();
            }




        }else {
            Log.d("Response failure",response.message()+"");
        }
    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
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
class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    private List<Search> movies;
    private Context context;
    private ListFragment frag_instance;

    public MovieAdapter(List<Search> movies,Context context,ListFragment frag_instance) {
        this.movies = movies;
        this.context=context;
        this.frag_instance=frag_instance;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Search obj=movies.get(position);
        holder.mtitle.setText(obj.getTitle());
        String title= obj.getTitle();
        holder.myear.setText(obj.getYear());
        String img_url=obj.getPoster();
        Glide.with(context).load(obj.getPoster()).asBitmap()
                .placeholder(R.drawable.loading).error(R.drawable.notfound).into(holder.mimageview);
        holder.mcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frag_instance.mlistener.onMovieSelected(obj.getImdbID());
            }
        });



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mtitle,myear;
        private ImageView mimageview;
        private CardView mcardview;
        public ViewHolder(View itemView) {
            super(itemView);
            mtitle=(TextView)itemView.findViewById(R.id.movie_title);
            myear=(TextView)itemView.findViewById(R.id.movie_year);
            mimageview=(ImageView)itemView.findViewById(R.id.movie_imageview);
            mcardview=(CardView)itemView.findViewById(R.id.cardview);

        }
    }
}
