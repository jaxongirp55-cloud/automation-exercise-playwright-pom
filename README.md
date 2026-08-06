# StayEase Hotel Room Booking Management System (SRMS)

An elegant, production-ready, pure Java in-memory console application built to satisfy Pearson BTEC Level 5 Unit 27: Advanced Programming assignment requirements. The system implements clean, robust Object-Oriented Programming (OOP) architectures, SOLID design principles, and enterprise design patterns.

---

## Table of Contents
1. [Project Description](#project-description)
2. [Key Features](#key-features)
3. [Language Justification](#language-justification)
4. [IDE Justification](#ide-justification)
5. [Architecture & Design Patterns](#architecture--design-patterns)
6. [Folder Structure](#folder-structure)
7. [Installation & Execution](#installation--execution)

---

## Project Description
The **StayEase Hotel Room Booking Management System (SRMS)** is an integrated management console built to orchestrate end-to-end hotel operations. It models crucial domain entities including rooms, customers, bookings, invoices, staff members, and food menu items.

The application facilitates:
- **Room Lifecycle States**: Models room transitions across `FREE`, `RESERVED`, `OCCUPIED`, `AWAITING_BILL`, and `CLEARED` states.
- **Dynamic Booking & Room Service**: Allows customers to reserve rooms and add multiple premium room service menu items (Starters, Mains, Desserts, Beverages, Combos).
- **Role-Based Access Control (RBAC)**: Models specific permissions for Manager, Receptionist, Front Desk, and Head Housekeeping roles.
- **Itemized Checkout Billing**: Supports dynamic pricing strategies, flat VAT calculation, tip handling, and bill splitting among guests.

---

## Key Features
- **Zero External Dependencies**: Standard Java compilation. Everything runs in-memory.
- **No Placeholders or TODOs**: Complete, compiling, production-quality source code.
- **Extensive Validation**: Gracefully handles non-numeric console inputs, duplicate IDs, invalid room transitions, and empty lists.
- **Pre-populated Automated Demo Data**: Generates 20 rooms, 15 customers, 10 employees, 30 premium menu items, and 20 historical invoices automatically upon startup.

---

## Language Justification
The project is built on **Java 17 (LTS)**. Java was selected as the language of implementation due to the following criteria:
1. **Strict Object-Oriented Paradigm**: Supports clean implementation of abstraction, inheritance, encapsulation, and polymorphism.
2. **Platform Independence**: Java's "Write Once, Run Anywhere" (WORA) philosophy guarantees portability across all target operating systems.
3. **Robust Memory Management**: Advanced automatic Garbage Collection prevents memory leaks during long-running server simulations.
4. **Compile-Time Safety**: Strong static typing reduces runtime crashes, finding syntax and logical structural errors during compilation.
5. **Standard API Collections**: Utilizing standard JDK classes (such as lists, maps, stacks, queues) eliminates dependency bloating.

---

## IDE Justification
**IntelliJ IDEA (Ultimate/Community Edition)** is the chosen Integrated Development Environment (IDE) for the following reasons:
1. **Intelligent Code Assistance**: Unmatched auto-completion, real-time code analysis, and refactoring safety.
2. **Built-in Compilation Tools**: Simplifies packages configuration, classpaths, and builds with simple compiler controls.
3. **Integrated Debugger**: Simplifies state inspection, execution tracing, and stack trace navigation.
4. **JavaDoc Generation**: Allows automated building of structured API documents directly from source code annotations.

---

## Architecture & Design Patterns

### 1. Command Pattern (Transactional Decoupling)
Used to orchestrate room booking preparations and cancellations.
- **Command (Interface)**: Declares transactional `execute()` and `undo()` operations.
- **PrepareBookingCommand**: Transition room to `RESERVED` and saves booking.
- **CancelBookingCommand**: Releases room to `FREE` and sets cancelled flags.
- **ReceptionQueue**: Implements a First-In-First-Out (FIFO) queue for pending customer check-ins, paired with a Last-In-First-Out (LIFO) undo history stack.

### 2. Strategy Pattern (Dynamic Pricing)
Allows the application to alter pricing models dynamically at runtime.
- **PricingStrategy (Interface)**: Defines custom invoice total calculation.
- **StandardPricing**: Standard rack rates.
- **HappyHourPricing**: Applies a 20% discount on entire bill.
- **LoyaltyPricing**: Applies a 10% discount and awards a free drink credit.

### 3. Singleton Pattern (Historical Logger)
Provides a globally accessible, thread-safe transactional logging registry.
- **BookingHistoryLog**: Utilizes double-checked locking to guarantee only one instance manages historical invoices. Supports filtering by room, date queries, and metrics on the most popular items.

### 4. Factory Pattern (Creational Encapsulation)
Encapsulates menu items instantiation.
- **RoomItemFactory**: Decouples services from direct subclass creation. Correctly instantiates concrete `Starter`, `MainCourse`, `Dessert`, `Beverage`, and `ComboMeal` instances using a simplified classification interface.

---

## Folder Structure
```
src/
├── Main.java               # Application entrypoint
├── command/
│   ├── Command.java
│   ├── PrepareBookingCommand.java
│   ├── CancelBookingCommand.java
│   └── ReceptionQueue.java
├── factory/
│   └── RoomItemFactory.java
├── models/
│   ├── Bill.java
│   ├── BillItem.java
│   ├── Booking.java
│   ├── BookingItem.java
│   ├── Customer.java
│   ├── FrontDesk.java
│   ├── HeadHousekeeping.java
│   ├── Manager.java
│   ├── Receptionist.java
│   ├── Room.java
│   ├── RoomStatus.java
│   └── Staff.java
├── roomitems/
│   ├── RoomItem.java
│   ├── Starter.java
│   ├── MainCourse.java
│   ├── Dessert.java
│   ├── Beverage.java
│   └── ComboMeal.java
├── services/
│   ├── BillingService.java
│   ├── BookingService.java
│   └── RoomService.java
├── singleton/
│   └── BookingHistoryLog.java
├── strategy/
│   ├── PricingStrategy.java
│   ├── StandardPricing.java
│   ├── HappyHourPricing.java
│   └── LoyaltyPricing.java
└── utils/
    ├── ConsoleMenu.java
    └── InputValidator.java
```

---

## Installation & Execution

### Prerequisites
- Java Development Kit (JDK) 17 or higher.
- Console terminal.

### Step 1: Compile all sources
Compile all Java classes to the `out` directory:
```bash
javac -d out src/roomitems/*.java src/models/*.java src/strategy/*.java src/command/*.java src/singleton/*.java src/factory/*.java src/services/*.java src/utils/*.java src/Main.java
```

### Step 2: Run the application
Execute the compiled binary from the root directory:
```bash
java -cp out Main
```
Upon startup, the system automatically runs the demo data generation and displays the primary management menu.
