package org.github.philipsonfhir.fhirproxy.bulkdata;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BulkdataResult {
    public static Bundle getResultBundle(Bundle bundle, String type, InstantType since) {
        List<Resource> resources = bundle.getEntry().stream()
                .map( entry -> entry.getResource())
                .collect(Collectors.toList());
        return getResultBundle(resources, type, since);
    }

    public static Bundle getResultBundle(List<Resource> resources, String type, InstantType since) {
        List<Resource> result = new ArrayList<>();
        resources.stream()
                .filter( resource -> (type == null || type.contains( resource.fhirType() )) )
                .filter( resource -> (since==null || !resource.getMeta().hasLastUpdatedElement() || since.before(resource.getMeta().getLastUpdated()) ))//TODO since
                .forEach( resource -> result.add( resource ) );

        Bundle resultBundle = new Bundle()
                .setTotal( result.size() )
                .setType( Bundle.BundleType.SEARCHSET );

        result.stream().forEach( resource -> {
            resultBundle.addEntry( new Bundle.BundleEntryComponent()
                    .setResource( resource )
            );
        } );

        resultBundle.setTotal(resultBundle.getEntry().size());
        return resultBundle;
    }
}
