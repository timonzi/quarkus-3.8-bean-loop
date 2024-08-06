package org.acme.impl;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.acme.ScopeEvent;
import org.acme.events.RequestEnd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EventListenerBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public void endRequest(@Observes @RequestEnd final ScopeEvent event) {
        logger.info("org.acme.impl.EventListenerBean.endRequest");
    }


}
