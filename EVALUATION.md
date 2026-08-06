# Software Architecture Evaluation: StayEase SRMS

This document provides an exhaustive, critical review of the architectural design decisions and software patterns implemented in the StayEase Hotel Room Booking Management System (SRMS). It is prepared to fulfill Pearson BTEC Level 5 Unit 27: Advanced Programming academic evaluation criteria.

---

## 1. Architectural Patterns & Decisions

The StayEase system has been built from the ground up adhering to **SOLID Principles** and **Clean Architecture**. The codebase segregates business logic (Services) from raw data models (Models), creation logic (Factories), and presentation (Console Menu).

By strictly decoupling packages, the core business domain is entirely isolated from UI changes, satisfying high maintainability indices.

---

## 2. Design Patterns Evaluation

### A. COMMAND PATTERN

#### Why implemented?
The Command pattern was selected to model hotel check-ins, bookings, and cancellation workflows as transactional, self-contained objects (`PrepareBookingCommand`, `CancelBookingCommand`). These transactions must support delayed execution (represented by the reception request queue) and transactional rollback capabilities (undoing actions).

#### Advantages:
1. **Queueing Capabilities**: System transactions can be queued up, logged, or transferred across different threads or network boundaries easily.
2. **Support for Undo/Redo**: By preserving prior states (`previousRoomStatus`), operations can be rolled back elegantly without risking data corruption.
3. **Decoupled Senders and Receivers**: The UI (`ConsoleMenu`) is completely ignorant of the underlying operational details; it simply registers commands with the `ReceptionQueue`.

#### Disadvantages:
1. **Class Multiplication**: Every discrete action requires a new concrete class file, increasing system overhead and code verbosity.
2. **State Storage Overhead**: Storing prior state histories inside commands occupies in-memory heap space, which can accumulate over long-running sessions.

#### Alternative Patterns:
An alternative would be the **Active Record / Transaction Script Pattern** where database transactions are committed directly. However, it lacks the modular flexibility of queuing and elegant multi-level undo capabilities.

---

### B. SINGLETON PATTERN

#### Why implemented?
The `BookingHistoryLog` serves as a singular, globally-shared historical accounting register. In any hospitality system, auditing, financial reporting, and item metric gathering require a single source of truth to avoid data synchronization conflicts across different reception desks.

#### Advantages:
1. **Guaranteed Singularity**: Restricts instantiation to exactly one thread-safe memory registry, preventing fragmented reporting.
2. **Global Access Point**: Provides a clean, standardized, static accessor (`getInstance()`) for any caller within the system.
3. **Thread Safety**: Implemented via **Double-Checked Locking** with `volatile` declarations to prevent race conditions during concurrent accesses.

#### Disadvantages:
1. **Global State Risk**: Singletons introduce global state, making Unit Testing difficult due to class state carry-overs between test runs.
2. **Hidden Dependencies**: Classes accessing the Singleton do not declare it explicitly in their constructors, obscuring architectural couplings.

#### Alternative Patterns:
**Dependency Injection (DI)** (such as Spring Bean Singleton scope) is a superior modern alternative. It retains a single object instance but injects it dynamically, solving mock-testing issues.

---

### C. STRATEGY PATTERN

#### Why implemented?
Hospitality environments use highly dynamic pricing structures that shift based on customer loyalty programs, happy hour windows, or seasonal demands. The Strategy pattern was implemented (`StandardPricing`, `HappyHourPricing`, `LoyaltyPricing`) to encapsulate these algorithm shifts, making pricing strategies easily interchangeable at runtime.

#### Advantages:
1. **Open-Closed Principle (OCP)**: New pricing algorithms (e.g. `WeekendSurgePricing`) can be added by creating a new Strategy class without modifying existing billing services.
2. **Runtime Flexibility**: Allows receptionists to adjust pricing models dynamically during checkout based on the guest's loyalty membership or active promotions.
3. **Elimination of Conditional Logic**: Replaces complex `if-else` or `switch` blocks within the billing calculator with simple polymorphism.

#### Disadvantages:
1. **Client Awareness**: The client (receptionist or calling system) must be aware of the differences between strategies to select the correct one.
2. **Increased Object Creation**: Creates additional lightweight class instances, adding minor garbage collection pressure.

#### Alternative Patterns:
The **Decorator Pattern** could be used to layer prices (e.g. applying standard rate + holiday tax + promo discount). However, for mutally exclusive pricing systems, Strategy remains the cleanest solution.

---

## 3. Extensibility & Scalability

The architecture of StayEase is designed to handle enterprise-level growth through the following pathways:

### Horizontal Service Scaling
Because services (`RoomService`, `BookingService`, `BillingService`) are entirely stateless, they can be scaled horizontally across multiple instances in a distributed microservices environment. All shared resource state (presently stored in-memory lists) can be seamlessly migrated to distributed caching layers (e.g., Redis) or relational database servers (e.g., PostgreSQL).

### Loose Coupling via Factories
The creational logic encapsulated within `RoomItemFactory` shields the rest of the application from modifications to specific menu subclasses. If new menu categories are added (e.g. `WellnessSpaServices`), only the factory and a new concrete class need to be registered, ensuring zero regressions in core service flows.

---

## 4. Recommendations for Future Improvements

While StayEase SRMS delivers a robust, production-grade console experience, the following improvements are recommended for real-world enterprise deployments:

1. **Persistent Relational Database (ORM)**: Integrate Hibernate or Spring Data JPA to persist transactions to a PostgreSQL database instead of memory, guaranteeing ACID transactions and data durability.
2. **Dependency Injection Framework (Spring Core)**: Replace the Singleton patterns and manual service instantiations with Spring IoC, improving testability through mock-injections.
3. **RESTful Web API Layer**: Wrap the services in Spring Boot REST controllers, exposing hotel operations to web/mobile clients.
4. **Security & Authentication (Spring Security)**: Implement role-based JSON Web Token (JWT) authorization to secure sensitive endpoints.
