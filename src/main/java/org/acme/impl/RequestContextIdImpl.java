package org.acme.impl;

import io.quarkus.arc.Arc;
import io.quarkus.arc.Unremovable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.acme.RequestId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
@RequestId
@Unremovable
public class RequestContextIdImpl extends AbstractContextId {

    private static final long serialVersionUID = 5668953571699169936L;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    protected void postConstruct() {
        String stack = Arrays.stream(Thread.currentThread().getStackTrace())
                .map(se -> "\n\t" + se.toString())
                .collect(Collectors.joining());
        logger.info("postConstruct stacktrace [{}] {}", getId(), stack);
    }


    @PreDestroy
    protected void preDestroy() {
        final var requestContext = Arc.container().requestContext();
        final var contextActive = requestContext.isActive();
        final var id = getId();
        logger.info("RequestContextIdImpl.preDestroy; [active: {}, id: {}]",
                contextActive, id);
        scopeChanged().fireRequestEnd(id);
    }

}
