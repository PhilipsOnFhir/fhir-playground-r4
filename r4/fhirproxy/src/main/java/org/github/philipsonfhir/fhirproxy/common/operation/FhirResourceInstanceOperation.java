package org.github.philipsonfhir.fhirproxy.common.operation;

import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedException;
import org.github.philipsonfhir.fhirproxy.controller.service.FhirServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;

import java.util.Map;

public interface FhirResourceInstanceOperation extends FhirOperation{

    String getResourceType();
}
