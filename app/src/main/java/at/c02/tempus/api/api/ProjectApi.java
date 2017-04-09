package at.c02.tempus.api.api;

import java.util.List;

import at.c02.tempus.api.CollectionFormats.*;
import at.c02.tempus.api.model.Project;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ProjectApi {
  /**
   * 
   * 
   * @return Call&lt;List&lt;Project&gt;&gt;
   */
  
  @GET("api/Project")
  Call<List<Project>> apiProjectGet();
    

}
