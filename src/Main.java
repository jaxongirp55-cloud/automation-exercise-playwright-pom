import services.RoomService;
import services.BookingService;
import services.BillingService;
import command.ReceptionQueue;
import utils.ConsoleMenu;

/**
 * Main application entrypoint for the StayEase Hotel Room Booking Management System (SRMS).
 * Connects all architectural components (Services, Singleton, Factory, Commands, Strategy)
 * and triggers the interactive Command Line interface.
 *
 * Built to satisfy Pearson BTEC Level 5 Unit 27: Advanced Programming requirements.
 */
public class Main {

    /**
     * Application main method.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Instantiate Services
        RoomService roomService = new RoomService();
        BookingService bookingService = new BookingService(roomService);
        BillingService billingService = new BillingService(bookingService);

        // Instantiate Command Pattern Invoker
        ReceptionQueue receptionQueue = new ReceptionQueue();

        // Instantiate driver UI Console Menu
        ConsoleMenu menu = new ConsoleMenu(roomService, bookingService, billingService, receptionQueue);

        // Start interactive system
        menu.start();
    }
}
