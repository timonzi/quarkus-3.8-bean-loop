package org.acme.impl;


import jakarta.enterprise.inject.spi.CDI;
import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.acme.ContextId;
import org.acme.ScopeChanged;


public abstract class AbstractContextId implements ContextId, Serializable {

    private static final long serialVersionUID = -8805706060703116949L;
    private final transient Lock lock = new ReentrantLock(false);

    private String contextId;


    @Override
    public String getId() {
        lock.lock();
        try {
            if (contextId == null) {
                contextId = UUID.randomUUID().toString();
            }
        } finally {
            lock.unlock();
        }
        return contextId;
    }


    protected ScopeChanged scopeChanged() {
        return CDI.current().select(ScopeChanged.class).get();
    }

}
