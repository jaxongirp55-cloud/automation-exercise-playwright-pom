# StayEase Hotel Room Booking Management System (SRMS)

## Project Description
StayEase Hotel Room Booking Management System (SRMS) is a robust, clean, and production-ready Java console application built to satisfy the rigorous assignment requirements of **Pearson BTEC Level 5 Unit 27: Advanced Programming**.

The system provides an in-memory solution for hotel booking, room status lifecycle management, itemized room service billing, dynamically-adjustable pricing strategies, and security-enforced staff management. It demonstrates best practices in **Object-Oriented Programming (OOP)**, **SOLID principles**, and **Enterprise Design Patterns** including Command, Factory, Singleton, and Strategy.

---

## Language Justification
This project is built using **Java 17**. Java was chosen for the following critical technical reasons:
1. **Strong Static Typing**: Prevents runtime errors common in dynamically typed languages, ensuring clean variable scopes and data-integrity.
2. **First-Class Object-Oriented Principles**: Clean implementation of OOP concepts (Encapsulation, Inheritance, Abstraction, Polymorphism).
3. **Robust Concurrency Utilities**: Supported via thread-safe collections (e.g., `ConcurrentHashMap`, `synchronized` collections), critical for managing state in enterprise hotel reservation portals.
4. **Platform Independence**: Run Anywhere capabilities (via JVM bytecode compilation), ensuring portability.
5. **Modern Java Features**: Java 17 features such as pattern matching, clean switch-expressions, and modern LocalDateTime APIs simplify business logic and increase readability.

---

## IDE Justification
The project is custom-configured to be compatible with **IntelliJ IDEA** (Ultimate and Community editions):
1. **Intelligent Refactoring**: Built-in support for renaming packages, extracting variables, and generating override signatures.
2. **Integrated Build Tools**: Native support for compiling complex, multi-package directory hierarchies without Maven/Gradle bloat.
3. **Seamless Test Runner**: Instant execution and profiling of code.
4. **Standard Package Structuring**: Clean mapping of directories matching standard project structures.

---

## Folder Structure
```
StayEase-SRMS/
│
├── src/
│   ├── Main.java               # Application entry point
│   │
│   ├── models/                 # Domain Entity models & Enums
│   │   ├── Customer.java
│   │   ├── Staff.java (Abstract)
│   │   ├── Manager.java
│   │   ├── Receptionist.java
│   │   ├── FrontDesk.java
│   │   ├── HeadHousekeeping.java
│   │   ├── Room.java
│   │   ├── RoomStatus.java (Enum)
│   │   ├── Booking.java
│   │   ├── BookingItem.java
│   │   ├── Bill.java
│   │   └── BillItem.java
│   │
│   ├── roomitems/              # Room Service Menu items
│   │   ├── RoomItem.java (Abstract)
│   │   ├── Starter.java
│   │   ├── MainCourse.java
│   │   ├── Dessert.java
│   │   ├── Beverage.java
│   │   └── ComboMeal.java
│   │
│   ├── factory/                # Creational Design Pattern
│   │   └── RoomItemFactory.java
│   │
│   ├── command/                # Behavioral Design Pattern (Transaction Queues & Undo)
│   │   ├── Command.java (Interface)
│   │   ├── PrepareBookingCommand.java
│   │   ├── CancelBookingCommand.java
│   │   └── ReceptionQueue.java
│   │
│   ├── singleton/              # Creational Design Pattern (Log Store)
│   │   └── BookingHistoryLog.java
│   │
│   ├── strategy/               # Behavioral Design Pattern (Dynamic Pricing)
│   │   ├── PricingStrategy.java (Interface)
│   │   ├── StandardPricing.java
│   │   ├── HappyHourPricing.java
│   │   └── LoyaltyPricing.java
│   │
│   ├── services/               # In-Memory Business Logic layers
│   │   ├── RoomService.java
│   │   ├── BookingService.java
│   │   └── BillingService.java
│   │
│   └── utils/                  # Safe Consoles & Input Validation
│       ├── ConsoleMenu.java
│       └── InputValidator.java
│
├── out/                        # Compiled JVM class files (.class)
│
├── README.md                   # Setup, installation, & language guides
└── EVALUATION.md               # Advanced Programming analysis & design evaluations
```

---

## Key Features
- **Room Lifecycle Transitions**: Standardize state flow `FREE` ➔ `RESERVED` ➔ `OCCUPIED` ➔ `AWAITING_BILL` ➔ `CLEARED` ➔ `FREE`.
- **Reception Queue**: Process new booking and cancellation requests via a processing queue, with full **Undo** capabilities.
- **Dynamic Pricing Strategy**: Change billing strategies at runtime. Switch between Standard (Normal), Happy Hour (20% Off), or Loyalty Pricing (10% Off + Free Drink).
- **Security & Authorization**: Real role-based permission checks (Manager, Receptionist, Front Desk, Housekeeping).
- **Persistent Historical Analytics**: Logging finalized transactions into a centralized, thread-safe Singleton ledger. Enables searching by date, room, and calculating the overall top ordered item.
- **Robust Input Validation**: Safely parsing console input with full regex and try-catch handling to ensure zero runtime application crashes.

---

## Installation & Setup

### Prerequisites
- **Java Development Kit (JDK) 17** or higher installed.
- Ensure `java` and `javac` are available in your system path environment:
  ```bash
  java --version
  ```

### Build Instructions
Compile all packages directly from the root repository directory:
```bash
javac -d out src/roomitems/*.java src/models/*.java src/strategy/*.java src/command/*.java src/singleton/*.java src/factory/*.java src/services/*.java src/utils/*.java src/Main.java
```

### Execution Instructions
Run the compiled application:
```bash
java -cp out Main
```

---
*StayEase Hotel Management System is developed with strict adherence to Clean Architecture, SOLID, and the DRY principle.*
