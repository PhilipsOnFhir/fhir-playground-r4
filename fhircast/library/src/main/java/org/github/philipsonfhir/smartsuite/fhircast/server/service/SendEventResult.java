package org.github.philipsonfhir.smartsuite.fhircast.server.service;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class SendEventResult {
    private boolean errorOccured = false;
    private List<String> messages = new ArrayList<>();

    public boolean hasErrorOccurred() {
        return errorOccured;
    }

    public void addError(String message){
        this.messages.add( message );
        this.errorOccured = true;
    }

    public List<String> getMessages() {
        return messages;
    }

    void update(SendEventResult webHookSendEventResult) {
        this.errorOccured = errorOccured || webHookSendEventResult.errorOccured;
        this.messages.addAll( webHookSendEventResult.messages );
    }
}
