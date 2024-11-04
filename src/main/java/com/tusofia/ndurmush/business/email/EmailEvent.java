package com.tusofia.ndurmush.business.email;

public class EmailEvent {
    public static final String PART_REQUEST_CREATED = "PART_REQUEST_CREATED";
    public static final String PART_OFFER_CREATED = "PART_OFFER_CREATED";
    public static final String PART_OFFER_ACCEPTED = "PART_OFFER_ACCEPTED";
    public static final String PART_OFFER_REJECTED = "PART_OFFER_REJECTED";
    public static final String PART_ORDER_CONFIRMED = "PART_ORDER_CONFIRMED";
    public static final String PART_ORDER_COMPLETED = "PART_ORDER_COMPLETED";

    private EmailEvent() {
    }
}
