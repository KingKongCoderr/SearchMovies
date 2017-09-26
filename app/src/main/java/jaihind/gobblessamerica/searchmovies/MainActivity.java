package jaihind.gobblessamerica.searchmovies;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements ListFragment.movieSelectedListener {
    boolean tablet_mode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        double wi=(double)width/(double)dm.xdpi;
        double hi=(double)height/(double)dm.ydpi;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);*/
        tablet_mode=isTablet(this);
        if(savedInstanceState==null) {

            //hello testing
            if(tablet_mode){
                getSupportFragmentManager().beginTransaction().
                        add(R.id.list_frag_container,new ListFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
            }else{
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new ListFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();}
        }
    }
    public static Boolean isTablet(Context context) {

        if ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {

            return true;
        }
        return false;
    }

    @Override
    public void onMovieSelected(String imdb_id) {
        String query=imdb_id;
        Log.d("imdb",query);
        if(tablet_mode){

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_frag_container,
                    DetailFragment.newInstance(imdb_id))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();

        }else{
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, DetailFragment.newInstance(imdb_id)).addToBackStack(null).commit();}
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
