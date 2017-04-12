package at.c02.tempus.api.api;

import java.util.List;

import at.c02.tempus.api.model.Booking;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;


public interface BookingApi {
  /**
   * 
   * 
   * @param id  (required)
   * @return Call&lt;Booking&gt;
   */
  
  @GET("api/Booking/{id}")
  Observable<Booking> apiBookingByIdGet(
    @retrofit2.http.Path("id") Integer id
  );

  /**
   * 
   * 
   * @return Call&lt;Void&gt;
   */
  
  @DELETE("api/Booking")
  Completable apiBookingDelete();
    

  /**
   * 
   * 
   * @param employeeId  (optional)
   * @param beginDate  (optional)
   * @param completed  (optional)
   * @return Call&lt;List&lt;Booking&gt;&gt;
   */
  
  @GET("api/Booking")
  Observable<List<Booking>> apiBookingGet(
    @retrofit2.http.Query("employeeId") Integer employeeId, @retrofit2.http.Query("beginDate") String beginDate, @retrofit2.http.Query("completed") Boolean completed
  );

  /**
   * 
   * 
   * @param booking  (optional)
   * @return Call&lt;Void&gt;
   */
  
  @Headers({
  	"Content-Type:application/json" 
  })
  @PATCH("api/Booking")
  Completable apiBookingPatch(
    @retrofit2.http.Body Booking booking
  );

  /**
   * 
   * 
   * @param booking  (optional)
   * @return Call&lt;Void&gt;
   */
  
  @Headers({
  	"Content-Type:application/json" 
  })
  @POST("api/Booking")
  Completable apiBookingPost(
    @retrofit2.http.Body Booking booking
  );

}
