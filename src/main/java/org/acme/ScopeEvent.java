package org.acme;

public class ScopeEvent {

    private final String scopeId;


    public ScopeEvent(final String scopeId) {
        this.scopeId = scopeId;
    }


    public String getScopeId() {
        return scopeId;
    }


    @Override
    public String toString() {
        return "ScopeEvent[" +
                "scopeId='" + scopeId + '\'' +
                ']';
    }
}
