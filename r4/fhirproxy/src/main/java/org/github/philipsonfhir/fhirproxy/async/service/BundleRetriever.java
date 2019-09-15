package org.github.philipsonfhir.fhirproxy.async.service;


import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BundleRetriever {
    private final FhirServer fhirServer;
    private final Bundle bundle;

    public BundleRetriever( FhirServer fhirServer, Bundle bundle) {
        this.fhirServer = fhirServer;
        this.bundle = bundle;
    }

    public List<Resource> retrieveAllResources() throws FHIRException {
        List<Resource> iBaseResourceList = new ArrayList<>();
        addAll( iBaseResourceList, bundle );
        Bundle currentBundle = bundle;
        while ( currentBundle.getLink(Bundle.LINK_NEXT)!=null ){
            Bundle nextBundle = fhirServer.loadPage( currentBundle );
            addAll( iBaseResourceList, nextBundle );
            currentBundle = nextBundle;
        }
        return Collections.unmodifiableList( iBaseResourceList );
    }

    private void addAll(List<Resource> iBaseResourceList, Bundle bundleToAdd) {
        bundleToAdd.getEntry().stream()
                .forEach( bundleEntryComponent -> iBaseResourceList.add( bundleEntryComponent.getResource() ) );
    }

    public Bundle addAllResourcesToBundle() throws FHIRException {
        Bundle resultBundle = bundle.copy();
        Bundle currentBundle = bundle;
        while ( currentBundle.getLink( Bundle.LINK_NEXT ) != null ) {
            Bundle nextPage = fhirServer.loadPage( currentBundle );
            resultBundle.getEntry().addAll( nextPage.getEntry() );
            currentBundle = nextPage;
        }
        resultBundle.setTotal( bundle.getEntry().size() );
        if ( resultBundle.getLink( Bundle.LINK_NEXT ) != null ) {
            resultBundle.getLink().remove( resultBundle.getLink( Bundle.LINK_NEXT ) );
        }
        return resultBundle;
    }
}
