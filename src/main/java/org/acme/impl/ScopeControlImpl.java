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
import org.acme.events.RequestBegin;
import org.acme.events.RequestEnd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ScopeControlImpl implements ScopeChanged {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    @RequestBegin
    Event<ScopeEvent> requestBegin;

    @Inject
    @RequestEnd
    Event<ScopeEvent> requestEnd;


    private AtomicBoolean active = new AtomicBoolean(true);


    @Override
    public void fireRequestEnd(final String requestId) {
        logger.info("de.wilken.p5.fw.base.impl.scopes.ScopeControlImpl.fireRequestEnd BEGIN [{}]", requestId);
        final ScopeEvent event = new ScopeEvent(requestId);
        this.requestEnd.fire(event);
        logger.info("de.wilken.p5.fw.base.impl.scopes.ScopeControlImpl.fireRequestEnd END [{}]", requestId);
    }


    @SuppressWarnings("java:S1172")
    public void requestBegin(@Observes @Initialized(RequestScoped.class) final Object o) {
        if (!active.get()) {
            return;
        }
        final ContextId contextId = requestContextId();
        if (contextId != null) {
            final ScopeEvent event = new ScopeEvent(contextId.getId());
            this.requestBegin.fire(event);
        }
    }


    private ContextId requestContextId() {
        try {
            return CDI.current().select(ContextId.class, new AnnotationLiteral<RequestId>() {
                private static final long serialVersionUID = 812452892807035671L;
            }).get();
        } catch (final NullPointerException e) {
            // sometimes null in shutdown phase
            this.logger.warn("request id could not be retrieved");
            return null;
        }
    }

}
