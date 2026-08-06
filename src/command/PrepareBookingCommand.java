package command;

import models.Booking;
import models.RoomStatus;
import java.util.List;

/**
 * Concrete Command pattern to prepare and register a new Room Booking.
 * Transitions associated Room status to RESERVED and appends the booking to the active log.
 */
public class PrepareBookingCommand implements Command {
    private final Booking booking;
    private final List<Booking> activeBookings;
    private final RoomStatus previousRoomStatus;
    private boolean isExecuted;

    /**
     * Constructs a PrepareBookingCommand.
     *
     * @param booking        The booking to execute.
     * @param activeBookings List of globally active hotel bookings.
     */
    public PrepareBookingCommand(Booking booking, List<Booking> activeBookings) {
        this.booking = booking;
        this.activeBookings = activeBookings;
        this.previousRoomStatus = booking.getRoom().getStatus();
        this.isExecuted = false;
    }

    @Override
    public void execute() {
        if (!isExecuted) {
            booking.getRoom().setStatus(RoomStatus.RESERVED);
            booking.setConfirmed(true);
            booking.setCancelled(false);
            if (!activeBookings.contains(booking)) {
                activeBookings.add(booking);
            }
            isExecuted = true;
        }
    }

    @Override
    public void undo() {
        if (isExecuted) {
            booking.getRoom().setStatus(previousRoomStatus);
            booking.setConfirmed(false);
            activeBookings.remove(booking);
            isExecuted = false;
        }
    }

    @Override
    public String getDescription() {
        return "Prepare Booking ID: " + booking.getBookingId() + " for Room: " + booking.getRoom().getRoomNumber() + " (Guest: " + booking.getCustomer().getName() + ")";
    }

    public Booking getBooking() {
        return booking;
    }
}
