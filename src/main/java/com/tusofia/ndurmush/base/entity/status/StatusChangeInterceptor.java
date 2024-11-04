package com.tusofia.ndurmush.base.entity.status;

import com.tusofia.ndurmush.base.entity.LongIdentifiable;
import com.tusofia.ndurmush.base.facade.BaseDbFacade;
import com.tusofia.ndurmush.base.facade.BaseDbFacadeImpl;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.StringUtils;
import com.tusofia.ndurmush.business.email.EmailArguments;
import com.tusofia.ndurmush.business.email.EmailEvent;
import com.tusofia.ndurmush.business.partoffer.entity.PartOffer;
import com.tusofia.ndurmush.business.partrequest.PartRequestState;
import com.tusofia.ndurmush.business.partrequest.entity.PartRequest;
import com.tusofia.ndurmush.business.partrequest.facade.PartRequestDbFacade;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.vertx.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.util.Optional;

@Interceptor
@StatusChangeEmailEventProducer
public class StatusChangeInterceptor {

    @Inject
    private EventBus eventBus;

    @Inject
    private PartRequestDbFacade partRequestDbFacade;

    @Inject
    @BaseDbFacadeImpl
    private BaseDbFacade baseDbFacade;

    @AroundInvoke
    public Object sendEmailEvents(final InvocationContext ctx) throws Exception {
        StatusAware<?> object = null;
        Object returnedObject = null;
        StatusAware<?> updatedObject = null;
        try {
            object = getStatusAwareObject(ctx.getParameters());
            returnedObject = ctx.proceed();
            updatedObject = getNewStatusAwareObject(returnedObject);
            String oldState = Optional.ofNullable(object).map(StatusAware::getState).map(Enum::name).orElse(null);
            String newState = Optional.ofNullable(updatedObject).map(StatusAware::getState).map(Enum::name).orElse(null);
            if (StringUtils.notEquals(oldState, newState)) {
                sendEmailEvent(updatedObject);
            }
        } catch (final Throwable throwable) { // NO SONAR
            // Any exception thrown while publishing email events must not
            // interfere with the application main logic
            // TODO: Add logger
            System.err.println(throwable.getCause());
        }
        return returnedObject;
    }

    private StatusAware<?> getStatusAwareObject(final Object[] parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            throw new IllegalStateException("Status aware object must be passed to the method");
        }
        Object object = parameters[0];
        if (!StatusAware.class.isAssignableFrom(object.getClass())) {
            throw new IllegalStateException("The object must inherit from StatusAware type. Actual type :"
                    + object.getClass().getName());
        }
        if (LongIdentifiable.class.isAssignableFrom(object.getClass())) {
            return (StatusAware<?>) QuarkusTransaction.requiringNew().call(() -> baseDbFacade.load(((LongIdentifiable) object).getId(), object.getClass(), true));
        }
        return (StatusAware<?>) object;
    }

    private StatusAware<?> getNewStatusAwareObject(final Object object) {
        if (null == object) {
            return null;
        }
        if (object instanceof StatusAware<?>) {
            return (StatusAware<?>) object;
        }
        return null;
    }

    private void sendEmailEvent(final StatusAware<?> object) {
        if (object instanceof PartRequest partRequest && PartRequestState.IN_PROGRESS.equals(object.getState())) {
            eventBus.request(EmailEvent.PART_REQUEST_CREATED, new EmailArguments()
                    .setPartRequest(partRequest)
                    .setSender(partRequest.getCreator())
                    .setRecipient(partRequest.getCreator())
                    .setUrl("http://localhost:4200/request/" + partRequest.getId()));
        } else if (object instanceof PartOffer offer) {
            PartRequest partRequest = partRequestDbFacade.load(offer.getRequestId());
            EmailArguments arguments = new EmailArguments()
                    .setPartOffer(offer)
                    .setPartRequest(partRequest)
                    .setSender(offer.getCreator())
                    .setRecipient(partRequest.getCreator())
                    .setUrl("http://localhost:4200/offer/" + offer.getId());
            switch (offer.getState()) {
                case SUBMITTED -> eventBus.request(EmailEvent.PART_OFFER_CREATED, arguments);
                case ACCEPTED -> eventBus.request(EmailEvent.PART_OFFER_ACCEPTED, arguments);
                case REJECTED -> eventBus.request(EmailEvent.PART_OFFER_REJECTED, arguments);
                case ORDER_CONFIRMED -> eventBus.request(EmailEvent.PART_ORDER_CONFIRMED, arguments);
                case ORDER_COMPLETED -> eventBus.request(EmailEvent.PART_ORDER_COMPLETED, arguments);
                default -> noop();
            }
        }
    }

    private void noop() {
    }

}
