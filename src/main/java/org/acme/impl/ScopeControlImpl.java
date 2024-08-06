package org.acme.impl;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;
import org.acme.ContextId;
import org.acme.RequestId;
import org.acme.ScopeChanged;
import org.acme.ScopeEvent;
import org.acme.events.RequestEnd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ScopeControlImpl implements ScopeChanged {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    @RequestEnd
    Event<ScopeEvent> requestEnd;


    @Override
    public void fireRequestEnd(final String requestId) {
        final ScopeEvent event = new ScopeEvent(requestId);
        this.requestEnd.fire(event);
    }


    @SuppressWarnings("java:S1172")
    public void requestBegin(@Observes @Initialized(RequestScoped.class) final Object o) {
        final ContextId contextId = requestContextId();
        if (contextId != null){
            logger.info("contextId: {}", contextId.getId());
        }
    }


    private ContextId requestContextId() {
        try {
            return CDI.current().select(ContextId.class, new AnnotationLiteral<RequestId>() {
                private static final long serialVersionUID = 812452892807035671L;
            }).get();
        } catch (final NullPointerException e) {
            this.logger.warn("request id could not be retrieved");
            return null;
        }
    }

}
