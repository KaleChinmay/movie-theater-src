package com.jpmc.theater;


import java.util.Objects;

/**
 * Reservation class
 * Contains customer object, showing object and count of audience the customer is booking
 */
public class Reservation {
    private Customer customer;
    private Showing showing;
    private int audienceCount;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public void setAudienceCount(int audienceCount) {
        this.audienceCount = audienceCount;
    }

    public Reservation(Customer customer, Showing showing, int audienceCount) {
        this.customer = customer;
        this.showing = showing;
        this.audienceCount = audienceCount;
    }

    /**
     * Get total fee for audience count for the showing instance.
     * @return total fee double
     */
    public double totalFee() {
        return showing.calculateTicketPriceAfterDiscount() * audienceCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return audienceCount == that.audienceCount && customer.equals(that.customer) && showing.equals(that.showing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, showing, audienceCount);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", showing=" + showing +
                ", audienceCount=" + audienceCount +
                '}';
    }
}