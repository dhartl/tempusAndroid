package at.c02.tempus.api.api;

import at.c02.tempus.api.CollectionFormats.*;
import at.c02.tempus.api.model.Employee;
import retrofit2.Call;
import retrofit2.http.GET;


public interface EmployeeApi {
  /**
   * 
   * 
   * @param userName  (required)
   * @return Call&lt;Employee&gt;
   */
  
  @GET("api/Employee/{userName}")
  Call<Employee> apiEmployeeByUserNameGet(
    @retrofit2.http.Path("userName") String userName
  );

}
