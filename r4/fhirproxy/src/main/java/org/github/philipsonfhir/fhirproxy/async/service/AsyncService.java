package org.github.philipsonfhir.fhirproxy.async.service;

import org.github.philipsonfhir.fhirproxy.async.model.AsyncSession;
import org.github.philipsonfhir.fhirproxy.common.fhircall.FhirCall;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AsyncService {
    public final static Logger logger = LoggerFactory.getLogger( AsyncService.class );
    private Map<String, AsyncSession> sessionMap = new TreeMap<>();

    public void deleteSession(String bulkdataserviceId) {
        sessionMap.remove(bulkdataserviceId);
    }

    public String newAyncGetSession(String callUrl, FhirCall fhirCall) {
        String sessionId = ""+ System.currentTimeMillis();

        AsyncSession asyncSession = new AsyncSession( callUrl, fhirCall );
        sessionMap.put( sessionId, asyncSession );

        return sessionId;
    }

    public AsyncSession getAsyncSession(String sessionId) {
        AsyncSession session = sessionMap.get( sessionId );
        return session;
    }

    public Status getSessionStatus(String bulkdataserviceId) {
        AsyncSession bulkDataSession = sessionMap.get( bulkdataserviceId );
        if ( bulkDataSession==null){
            return Status.UNKNOWN;
        }
        if ( bulkDataSession.isReady()){
            return Status.READY;
        } else
        {
            return Status.PROCESSING;
        }

    }

}
