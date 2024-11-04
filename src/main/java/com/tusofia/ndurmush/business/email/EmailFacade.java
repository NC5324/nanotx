package com.tusofia.ndurmush.business.email;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class EmailFacade {

    @ConfigProperty(name = "quarkus.mailer.from")
    private String senderEmail;

    @Inject
    private ReactiveMailer reactiveMailer;

    @ConsumeEvent(EmailEvent.PART_REQUEST_CREATED)
    public void sendPartRequestCreatedEmail(final EmailArguments arguments) {
        reactiveMailer.send(createPartRequestCreatedEmail(arguments));
    }

    private Mail createPartRequestCreatedEmail(final EmailArguments arguments) {
        return Mail.withText(arguments.getRecipient().getEmail(), String.format("nanotx - New part request created: %s - %s",
                arguments.getPartRequest().getId(), arguments.getPartRequest().getComponentName()), "Follow the link in order to continue processing: " + arguments.getUrl());
    }

    @ConsumeEvent(EmailEvent.PART_OFFER_CREATED)
    public void sendPartOfferCreatedEmail(final EmailArguments arguments) {
        reactiveMailer.send(createPartOfferCreatedEmail(arguments));
    }

    private Mail createPartOfferCreatedEmail(final EmailArguments arguments) {
        return Mail.withText(arguments.getRecipient().getEmail(), String.format("nanotx - New offer created for part request: %s - %s",
                arguments.getPartRequest().getId(), arguments.getPartRequest().getComponentName()), "Follow the link in order to continue processing: " + arguments.getUrl());
    }

    @ConsumeEvent(EmailEvent.PART_OFFER_ACCEPTED)
    public void sendPartOfferAcceptedEmail(final EmailArguments arguments) {
        reactiveMailer.send(createPartOfferAcceptedEmail(arguments));
    }

    private Mail createPartOfferAcceptedEmail(final EmailArguments arguments) {
        return Mail.withText(arguments.getRecipient().getEmail(), String.format("nanotx - Offer %s for part request %s - %s was accepted",
                arguments.getPartOffer().getId(), arguments.getPartRequest().getId(),
                arguments.getPartRequest().getComponentName()), "Follow the link in order to continue processing: " + arguments.getUrl());
    }

    @ConsumeEvent(EmailEvent.PART_OFFER_REJECTED)
    public void sendPartOfferRejectedEmail(final EmailArguments arguments) {
        reactiveMailer.send(createPartOfferRejectedEmail(arguments));
    }

    private Mail createPartOfferRejectedEmail(final EmailArguments arguments) {
        return Mail.withText(arguments.getRecipient().getEmail(), String.format("nanotx - Offer %s for part request %s - %s was rejected",
                arguments.getPartOffer().getId(), arguments.getPartRequest().getId(),
                arguments.getPartRequest().getComponentName()), "Follow the link in order to continue processing: " + arguments.getUrl());
    }

    @ConsumeEvent(EmailEvent.PART_ORDER_CONFIRMED)
    public void sendPartOrderConfirmedEmail(final EmailArguments arguments) {
        reactiveMailer.send(createPartOrderConfirmedEmail(arguments));
    }

    private Mail createPartOrderConfirmedEmail(final EmailArguments arguments) {
        return Mail.withText(arguments.getRecipient().getEmail(), String.format("nanotx - Order confirmed for offer %s for part request %s - %s",
                arguments.getPartOffer().getId(), arguments.getPartRequest().getId(),
                arguments.getPartRequest().getComponentName()), "Follow the link in order to continue processing: " + arguments.getUrl());
    }

    @ConsumeEvent(EmailEvent.PART_ORDER_COMPLETED)
    public void sendPartOrderCompletedEmail(final EmailArguments arguments) {
        reactiveMailer.send(createPartOrderCompletedEmail(arguments));
    }

    private Mail createPartOrderCompletedEmail(final EmailArguments arguments) {
        return Mail.withText(arguments.getRecipient().getEmail(), String.format("nanotx - Order completed for offer %s for part request %s - %s",
                arguments.getPartOffer().getId(), arguments.getPartRequest().getId(),
                arguments.getPartRequest().getComponentName()), "Follow the link in order to continue processing: " + arguments.getUrl());
    }

}
