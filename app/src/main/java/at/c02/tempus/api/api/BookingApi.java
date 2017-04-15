package at.c02.tempus.api.api;

import java.util.Date;
import java.util.List;

import at.c02.tempus.api.model.Booking;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface BookingApi {
    /**
     * @param id (required)
     * @return Call&lt;Booking&gt;
     */

    @GET("api/Booking/{id}")
    Observable<Booking> getBookingById(
            @Path("id") Integer id
    );

    /**
     * @return Call&lt;Void&gt;
     */

    @DELETE("api/Booking")
    Completable deleteBooking(@Query("id") Integer bookingId);


    /**
     * @param employeeId (optional)
     * @param beginDate  (optional)
     * @param completed  (optional)
     * @return Call&lt;List&lt;Booking&gt;&gt;
     */

    @GET("api/Booking")
    Observable<List<Booking>> findBookingsForEmployeeAndDate(
            @Query("employeeId") Integer employeeId,
            @Query("beginDate") String beginDate,
            @Query("completed") Boolean completed
    );

    /**
     * @param booking (optional)
     * @return Call&lt;Void&gt;
     */

    @Headers({
            "Content-Type:application/json"
    })
    @PATCH("api/Booking")
    Observable<Booking> updateBooking(
            @Body Booking booking
    );

    /**
     * @param booking (optional)
     * @return Call&lt;Void&gt;
     */

    @Headers({
            "Content-Type:application/json"
    })
    @POST("api/Booking")
    Observable<Booking> createBooking(
            @Body Booking booking
    );

}
