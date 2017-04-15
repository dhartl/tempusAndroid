package at.c02.tempus.api.api;

import java.util.List;

import at.c02.tempus.api.model.Project;
import io.reactivex.Observable;
import retrofit2.http.GET;


public interface ProjectApi {
    /**
     * @return Call&lt;List&lt;Project&gt;&gt;
     */

    @GET("api/Project")
    Observable<List<Project>> loadProjects();


}
