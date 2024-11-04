package com.tusofia.ndurmush.business.email;

import com.tusofia.ndurmush.business.partoffer.entity.PartOffer;
import com.tusofia.ndurmush.business.partrequest.entity.PartRequest;
import com.tusofia.ndurmush.business.user.entity.User;

public class EmailArguments {

    private PartRequest partRequest;

    private PartOffer partOffer;

    private User sender;

    private User recipient;

    private String url;

    public PartRequest getPartRequest() {
        return partRequest;
    }

    public EmailArguments setPartRequest(PartRequest partRequest) {
        this.partRequest = partRequest;
        return this;
    }

    public PartOffer getPartOffer() {
        return partOffer;
    }

    public EmailArguments setPartOffer(PartOffer partOffer) {
        this.partOffer = partOffer;
        return this;
    }

    public User getSender() {
        return sender;
    }

    public EmailArguments setSender(User sender) {
        this.sender = sender;
        return this;
    }

    public User getRecipient() {
        return recipient;
    }

    public EmailArguments setRecipient(User recipient) {
        this.recipient = recipient;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public EmailArguments setUrl(final String url) {
        this.url = url;
        return this;
    }
}
