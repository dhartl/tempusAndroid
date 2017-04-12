package at.c02.tempus.service;

import at.c02.tempus.api.api.BookingApi;
import at.c02.tempus.db.entity.BookingEntity;
import at.c02.tempus.db.repository.BookingRepository;

/**
 * Created by Daniel on 09.04.2017.
 */

public class BookingService {
    private BookingApi bookingApi;
    private BookingRepository bookingRepository;

    public BookingService(BookingApi bookingApi, BookingRepository bookingRepository) {
        this.bookingApi = bookingApi;
        this.bookingRepository = bookingRepository;
    }

    public BookingEntity createOrUpdateBooking(BookingEntity booking) {
        // TODO: BookingStatus
        return bookingRepository.createOrUpdate(booking);
    }

    public void deleteBooking(BookingEntity booking) {
        // TODO: BookingStatus
        bookingRepository.delete(booking);
    }
}
