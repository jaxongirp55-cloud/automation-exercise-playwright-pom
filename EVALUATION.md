# Advanced Programming Evaluation: StayEase Hotel Room Booking System (SRMS)

This document provides a comprehensive software engineering and architectural evaluation of the design patterns and structures used in the StayEase Hotel Room Booking Management System (SRMS), fulfilling the academic and technical requirements of the **Pearson BTEC Level 5 Unit 27: Advanced Programming** assignment.

---

## 1. THE COMMAND PATTERN

### Why Command Pattern?
In a hotel reception environment, booking and cancellation requests are asynchronous, transactional operations. The Command Pattern was implemented to encapsulate these requests as object-oriented entities (`PrepareBookingCommand` and `CancelBookingCommand`). This decouples the client triggering the action (e.g., reception terminal) from the service execution layers (`BookingService`).

Furthermore, we need to handle transaction history and undo capabilities. By transforming actions into objects containing an `execute()` and `undo()` method, we can stack commands in a history log and reverse state changes exactly in reverse order.

### Advantages
- **Undo/Redo Capability**: Allows receptionists to easily undo the last action (e.g., undo a mistake reservation or restore a mistakenly cancelled booking) with zero data loss.
- **Task Queuing**: Commands are enqueued in `ReceptionQueue`. If the system is busy, it processes them sequentially (`processNext()`), ensuring thread-safe execution of room changes.
- **Extensibility**: Adding new operations (e.g., `UpgradingRoomCommand` or `OrderingServiceCommand`) only requires creating a new subclass of `Command` without modifying existing queue or invoker code.

### Disadvantages
- **Class Proliferation**: Every unique operation requires a new concrete class (`PrepareBookingCommand`, `CancelBookingCommand`), increasing the file count of the codebase.
- **Complexity**: High abstraction layers require developers to understand the interaction between the Invoker (`ReceptionQueue`), Receiver (`BookingService`), and concrete Commands.

### Alternative Patterns
- **Active Record / Direct Service Method Invocation**: Invoking service methods directly (e.g., `bookingService.createBooking(...)`) without intermediate command classes. This is simpler to write but makes adding queuing, transaction logging, or undo features extremely complex and brittle.

---

## 2. THE SINGLETON PATTERN

### Why Singleton Pattern?
The system requires a single, globally accessible transaction ledger (`BookingHistoryLog`) to record all completed, finalized payments and logs. Having multiple logs would cause data inconsistency, fragmented reports, and synchronization bugs.

By utilizing the Singleton pattern with the **Bill Pugh Helper** method, we ensure that exactly one thread-safe instance of the history log exists and can be accessed dynamically by both `BillingService` (to write logs) and `ConsoleMenu` (to view reports).

### Advantages
- **Controlled Shared Instance**: Restricts instantiation, preventing developers from creating duplicate logs that fragment memory state.
- **Global Access**: Offers a clean gateway `BookingHistoryLog.getInstance()` for multiple services without passing the log reference around.
- **Lazy Initialization & Thread-Safety**: The Bill Pugh inner class holds the instance, so it is only loaded when referenced, and the JVM guarantees safety without heavy synchronization penalties.

### Disadvantages
- **Tight Coupling**: Singletons introduce global state, making Unit Testing harder because mocks cannot easily isolate the Singleton lifecycle across parallel tests.
- **Violation of Single Responsibility (SRP)**: The singleton manages both its class lifecycle and its business operations (storing logs, calculating statistics).

### Alternative Patterns
- **Dependency Injection (DI)**: Passing a single instance of `BookingHistoryLog` to service constructors via a DI framework like Spring Core or Guice. This is cleaner and more testable, but Unit 27 explicitly forbids external framework dependencies (Spring Boot, etc.) and specifies a pure Java console system.

---

## 3. THE STRATEGY PATTERN

### Why Strategy Pattern?
Hotel pricing fluctuates dynamically based on promotions, member loyalty, seasonal happy hours, and peak bookings. Hardcoding these rules into `Bill` creates giant, unmaintainable nested `if-else` blocks.

The Strategy Pattern defines a standard interface (`PricingStrategy`) and encapsulates unique algorithms into isolated classes (`StandardPricing`, `HappyHourPricing`, `LoyaltyPricing`). The active pricing strategy is attached to `BillingService` and can be switched dynamically at runtime, applying discounts immediately to newly generated bills.

### Advantages
- **Eliminates Conditional Complexity**: Replaces nested condition chains with clean, polymorphic method calls (`strategy.calculateTotal(booking)`).
- **Runtime Adaptability**: Allows the hotel manager to instantly change active hotel-wide pricing (e.g., switch Standard ➔ Happy Hour) with one menu click.
- **Open-Closed Principle (OCP)**: New pricing structures (e.g., `SeasonalWeekendPricing`) can be added by creating a new Strategy class without modifying existing billing classes.

### Disadvantages
- **Increased Class Count**: Similar to Command, adding more strategies increases the number of Java source files.
- **Client Awareness**: The client/menu code must be aware of the different strategy classes to choose and set the appropriate one.

### Alternative Patterns
- **Template Method Pattern**: Relies on subclassing (e.g., creating a `HappyHourBill` subclass of `Bill`). This is highly rigid because a bill's pricing model cannot be changed after instantiation, whereas Strategy separates calculations dynamically from the entity.

---

## 4. THE FACTORY PATTERN

### Why Factory Pattern?
The room service menu offers multiple food and beverage types (`Starter`, `MainCourse`, `Dessert`, `Beverage`, `ComboMeal`), each containing different internal representations (such as `alcoholic` boolean or `description` string).
Creating these classes directly using `new` operator couples the client to the specific subclasses and increases dependency. `RoomItemFactory` abstracts this instantiation process.

### Advantages
- **Decouples Instantiation**: The client only calls `RoomItemFactory.createItem(...)` and receives a polymorphic `RoomItem` interface reference, unaware of the specific subclass being initialized.
- **Single Point of Configuration**: If item creation details change, the changes are restricted to the Factory class.

### Disadvantages
- **Limited Flexibility**: Adding a subclass with completely different constructor parameters requires modifying the general Factory signature.

---

## 5. SYSTEM SCALABILITY & POSSIBLE IMPROVEMENTS

Although the current system is production-ready, highly modular, and fully functional, the following improvements would prepare it for a large-scale enterprise environment:

1. **Database Persistence Integration**:
   - *Current*: Stores all states in-memory (disappears upon system termination).
   - *Improvement*: Introduce a persistent data layer using JPA/Hibernate or JDBC connected to PostgreSQL/MySQL, wrapping services in transactional contexts.
2. **RESTful Web Services & GUI**:
   - *Current*: Console Application.
   - *Improvement*: Transition to a modern multi-tier web application. Expose REST endpoints using Spring Boot or Jakarta EE, and build a front-end UI using React or Angular.
3. **Robust Security Framework**:
   - *Current*: Simple role checks via checking Staff properties.
   - *Improvement*: Integrate a standardized security framework like Spring Security or JAAS to handle authentication, password hashing, and token-based session tokens.
4. **Enhanced Dependency Injection**:
   - *Current*: Manual construction and service reference linking in `Main.java`.
   - *Improvement*: Use a DI framework to manage bean lifecycles and increase decoupling.
