import services.*;
import utils.ConsoleMenu;
import java.util.Scanner;

/**
 * Main application entrypoint for the StayEase Hotel Room Booking Management System (SRMS).
 * Instantiates backend services, seeds demo data, and loads the primary console menu.
 */
public class Main {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        System.out.println("Initializing StayEase System Core services...");

        // 1. Instantiate Core Business Logic Services
        RoomService roomService = new RoomService();
        BookingService bookingService = new BookingService();

        // Settle a standard 15% VAT tax rate
        BillingService billingService = new BillingService(0.15);

        // 2. Instantiate Primary User Interface Coordinator
        ConsoleMenu menu = new ConsoleMenu(roomService, bookingService, billingService);

        // 3. Automatically Seed Complete Demo Data to ensure instant readiness
        System.out.println("Seeding automated standard demo datasets...");
        menu.handleGenerateDemoData();

        // 4. Open Input stream and hand control to console menu
        try (Scanner scanner = new Scanner(System.in)) {
            menu.run(scanner);
        } catch (Exception e) {
            System.err.println("Fatal Error in StayEase Application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
