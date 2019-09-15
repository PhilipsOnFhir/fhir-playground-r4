package org.github.philipsonfhir.cdshooks.test.model;

public interface CdsServiceImplementationInterface {
    CdsService getCdsService();

    CdsServiceResponse getCallCdsService(CdsServiceCallBody body);
}
