package models;

import graph.Node;

/**
 * Represents a specific Warehouse model, extending the standard Node properties.
 * Satisfies standard OOP HND requirements (Inheritance, Overrides).
 *
 * @author Senior Java Software Architect
 */
public class Warehouse extends Node {
    private String physicalAddress;
    private int capacityUnits;

    /**
     * Warehouse constructor.
     * @param name Unique warehouse identification name.
     * @param physicalAddress Mailing/shipping address coordinates.
     * @param capacityUnits Maximum capacity of inventory.
     */
    public Warehouse(String name, String physicalAddress, int capacityUnits) {
        super(name);
        if (physicalAddress == null || physicalAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Physical address cannot be null or empty.");
        }
        if (capacityUnits < 0) {
            throw new IllegalArgumentException("Capacity units cannot be negative.");
        }
        this.physicalAddress = physicalAddress.trim();
        this.capacityUnits = capacityUnits;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        if (physicalAddress == null || physicalAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Physical address cannot be null or empty.");
        }
        this.physicalAddress = physicalAddress.trim();
    }

    public int getCapacityUnits() {
        return capacityUnits;
    }

    public void setCapacityUnits(int capacityUnits) {
        if (capacityUnits < 0) {
            throw new IllegalArgumentException("Capacity units cannot be negative.");
        }
        this.capacityUnits = capacityUnits;
    }

    @Override
    public String toString() {
        return String.format("Warehouse[Name=%s, Address=%s, Capacity=%d units]",
                getName(), physicalAddress, capacityUnits);
    }
}
