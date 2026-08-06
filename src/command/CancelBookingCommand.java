package command;

import models.Booking;
import models.RoomStatus;
import java.util.List;

/**
 * Concrete Command pattern to cancel an existing Room Booking.
 * Sets the booking status to cancelled, releases the room back to FREE,
 * and maintains restoration state for undo transactions.
 */
public class CancelBookingCommand implements Command {
    private final Booking booking;
    private final List<Booking> activeBookings;
    private final RoomStatus previousRoomStatus;
    private boolean isExecuted;

    /**
     * Constructs a CancelBookingCommand.
     *
     * @param booking        The booking to cancel.
     * @param activeBookings List of globally active hotel bookings.
     */
    public CancelBookingCommand(Booking booking, List<Booking> activeBookings) {
        this.booking = booking;
        this.activeBookings = activeBookings;
        this.previousRoomStatus = booking.getRoom().getStatus();
        this.isExecuted = false;
    }

    @Override
    public void execute() {
        if (!isExecuted) {
            booking.setCancelled(true);
            booking.getRoom().setStatus(RoomStatus.FREE);
            isExecuted = true;
        }
    }

    @Override
    public void undo() {
        if (isExecuted) {
            booking.setCancelled(false);
            booking.getRoom().setStatus(previousRoomStatus);
            if (!activeBookings.contains(booking)) {
                activeBookings.add(booking);
            }
            isExecuted = false;
        }
    }

    @Override
    public String getDescription() {
        return "Cancel Booking ID: " + booking.getBookingId() + " for Room: " + booking.getRoom().getRoomNumber() + " (Guest: " + booking.getCustomer().getName() + ")";
    }

    public Booking getBooking() {
        return booking;
    }
}
