package command;

import models.Booking;
import models.RoomStatus;
import services.BookingService;

/**
 * Concrete command to prepare (create and reserve) a room booking.
 * Part of the Command design pattern.
 */
public class PrepareBookingCommand implements Command {
    private BookingService bookingService;
    private Booking booking;
    private RoomStatus previousStatus;

    /**
     * Constructor for PrepareBookingCommand.
     * @param bookingService The BookingService instance managing bookings.
     * @param booking The Booking object to process.
     */
    public PrepareBookingCommand(BookingService bookingService, Booking booking) {
        this.bookingService = bookingService;
        this.booking = booking;
        this.previousStatus = booking.getRoom().getStatus();
    }

    @Override
    public void execute() {
        bookingService.addBookingDirectly(booking);
        previousStatus = booking.getRoom().getStatus();
        booking.getRoom().setStatus(RoomStatus.RESERVED);
        booking.setConfirmed(true);
    }

    @Override
    public void undo() {
        bookingService.removeBookingDirectly(booking.getBookingId());
        booking.getRoom().setStatus(previousStatus);
        booking.setConfirmed(false);
    }

    @Override
    public String getDescription() {
        return String.format("Prepare Booking #%s: Room #%d for %s",
                booking.getBookingId(), booking.getRoom().getRoomNumber(), booking.getCustomer().getName());
    }
}
