package command;

import models.Booking;
import models.RoomStatus;
import services.BookingService;

/**
 * Concrete command to cancel an existing room booking.
 * Part of the Command design pattern.
 */
public class CancelBookingCommand implements Command {
    private BookingService bookingService;
    private Booking booking;
    private RoomStatus previousStatus;
    private boolean previousConfirmed;

    /**
     * Constructor for CancelBookingCommand.
     * @param bookingService The BookingService instance managing bookings.
     * @param booking The Booking object to cancel.
     */
    public CancelBookingCommand(BookingService bookingService, Booking booking) {
        this.bookingService = bookingService;
        this.booking = booking;
        this.previousStatus = booking.getRoom().getStatus();
        this.previousConfirmed = booking.isConfirmed();
    }

    @Override
    public void execute() {
        bookingService.removeBookingDirectly(booking.getBookingId());
        previousStatus = booking.getRoom().getStatus();
        previousConfirmed = booking.isConfirmed();

        booking.getRoom().setStatus(RoomStatus.FREE);
        booking.setConfirmed(false);
    }

    @Override
    public void undo() {
        bookingService.addBookingDirectly(booking);
        booking.getRoom().setStatus(previousStatus);
        booking.setConfirmed(previousConfirmed);
    }

    @Override
    public String getDescription() {
        return String.format("Cancel Booking #%s: Room #%d for %s",
                booking.getBookingId(), booking.getRoom().getRoomNumber(), booking.getCustomer().getName());
    }
}
