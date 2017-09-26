package jaihind.gobblessamerica.searchmovies.Model;

import java.util.List;

/**
 * Created by nande on 2/1/2017.
 */

public class Result {
    private List<Search> Search ;
    private String totalResults;
    private String Response;

    public List<Search> getSearch() {
        return Search;
    }

    public void setSearch(List<Search> search) {
        this.Search = search;
    }
    public String getTotalResults(){
        return totalResults;
    }
    public void setTotalResults(String input){
        this.totalResults = input;
    }
    public String getResponse(){
        return Response;
    }
    public void setResponse(String input){
        this.Response = input;
    }
}
