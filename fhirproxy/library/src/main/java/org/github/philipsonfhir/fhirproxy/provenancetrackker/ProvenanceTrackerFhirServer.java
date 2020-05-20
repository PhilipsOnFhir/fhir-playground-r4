//package org.github.philipsonfhir.fhirproxy.provenancetrackker;
//
//import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.rest.api.MethodOutcome;
//import org.github.philipsonfhir.fhirproxy.common.FhirProxyError;
//import org.github.philipsonfhir.fhirproxy.common.FhirProxyException;
//import org.github.philipsonfhir.fhirproxy.common.FhirProxyNotImplementedException;
//import org.github.philipsonfhir.fhirproxy.common.fhirserver.IFhirServer;
//import org.github.philipsonfhir.fhirproxy.common.util.ReferenceUtil;
//import org.hl7.fhir.exceptions.FHIRException;
//import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
//import org.hl7.fhir.instance.model.api.IBaseResource;
//import org.hl7.fhir.r4.model.*;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class ProvenanceTrackerFhirServer implements IFhirServer {
//
//    private final IFhirServer fhirServer;
//    private final Provenance provenance;
//    private Map<String,Reference> readResources = new HashMap<>();
//    private Map<String,Reference> targetResources = new HashMap<>();
//
//    public ProvenanceTrackerFhirServer(IFhirServer fhirServer, Reference agentReference, @NotNull String policy ) throws FhirProxyException {
//        this.fhirServer = fhirServer;
//
//        Device device = (Device) new Device()
//                .setStatus( Device.FHIRDeviceStatus.ACTIVE)
//                .addStatusReason( new CodeableConcept().addCoding(new Coding()
//                        .setSystem("http://hl7.org/fhir/ValueSet/device-status-reason")
//                        .setCode("online")
//                        .setDisplay("Online")
//                ))
//                .setManufacturer("Philips Research")
//                .addDeviceName( new Device.DeviceDeviceNameComponent()
//                        .setName("Provenance Tracker")
//                        .setType( Device.DeviceNameType.MODELNAME)
//                )
//                .setId("FhirProcy Provenance Tracker");
//
//        this.provenance = new Provenance()
//                .setRecorded( new Date( ))
//                .setOccurred( new Period().setStart(new Date()))
//                .addPolicy( policy )
//                .addAgent( new Provenance.ProvenanceAgentComponent()
//                        .addRole( new CodeableConcept().addCoding( new Coding()
//                                .setSystem( "http://terminology.hl7.org/CodeSystem/provenance-participant-type")
//                                .setCode("assembler")
//                                .setDisplay("Assembler")
//                        ))
//                        .setWho( ReferenceUtil.getReference(device) )
//                );
//
//        if ( agentReference!=null ) {
//            provenance.addAgent(new Provenance.ProvenanceAgentComponent()
//                    .setWho(agentReference)
//                    .addRole(new CodeableConcept().addCoding(new Coding()
//                            .setSystem("http://terminology.hl7.org/CodeSystem/provenance-participant-type")
//                            .setCode("enterer")
//                            .setDisplay("Enterer")
//                    ))
//            );
//        }
//        this.fhirServer.doPut( device );
//        MethodOutcome methodOutcome = this.fhirServer.doPost(provenance);
//        provenance.setId( methodOutcome.getId() );
//    }
//
//    private void updateProvenance() throws FhirProxyException {
//        this.provenance.setTarget(this.targetResources.values().stream().collect(Collectors.toList()));
//        this.provenance.setEntity(
//                this.readResources.values().stream()
//                        .map(reference -> new Provenance.ProvenanceEntityComponent().setWhat(reference))
//                        .collect(Collectors.toList())
//        );
//        Period period = (Period) this.provenance.getOccurred();
//        period.setEnd(new Date());
//        this.fhirServer.doPut(provenance);
//    }
//
//    @Override
//    public CapabilityStatement getCapabilityStatement() throws FhirProxyException {
//        return fhirServer.getCapabilityStatement();
//    }
//
//    @Override
//    public IBaseResource doGet(ResourceType resourceType, Map<String, String> queryParams) {
//        IBaseResource result = this.fhirServer.doGet(resourceType, queryParams);
//        Reference reference = ReferenceUtil.getReference((Resource) result);
//        this.readResources.put( reference.getReference(), reference );
//        try {
//            updateProvenance();
//        } catch (FhirProxyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public IBaseResource doGet(String resourceType, Map<String, String> queryParams) {
//        IBaseResource result = this.fhirServer.doGet(resourceType, queryParams );
//        Reference reference = ReferenceUtil.getReference((Resource) result);
//        this.readResources.put( reference.getReference(), reference );
//        try {
//            updateProvenance();
//        } catch (FhirProxyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public IBaseResource doGet(ResourceType resourceType, String resourceId, Map<String, String> queryParams) {
//        IBaseResource result = this.fhirServer.doGet(resourceType, resourceId, queryParams );
//        Reference reference = ReferenceUtil.getReference((Resource) result);
//        this.readResources.put( reference.getReference(), reference );
//        try {
//            updateProvenance();
//        } catch (FhirProxyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public IBaseResource doGet(String resourceType, String resourceId, Map<String, String> queryParams) {
//        IBaseResource result = this.fhirServer.doGet(resourceType, resourceId, queryParams );
//        Reference reference = ReferenceUtil.getReference((Resource) result);
//        this.readResources.put( reference.getReference(), reference );
//        try {
//            updateProvenance();
//        } catch (FhirProxyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public IBaseResource doGet(String resourceType, String resourceId, String operationName, Map<String, String> queryParams) {
//        IBaseResource result = this.fhirServer.doGet(resourceType, resourceId, queryParams );
//        Reference reference = ReferenceUtil.getReference((Resource) result);
//        this.readResources.put( reference.getReference(), reference );
//        try {
//            updateProvenance();
//        } catch (FhirProxyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public IBaseResource doGet(String url) {
//        IBaseResource result = this.fhirServer.doGet( url );
//        Reference reference = ReferenceUtil.getReference((Resource) result);
//        this.readResources.put( reference.getReference(), reference );
//        try {
//            updateProvenance();
//        } catch (FhirProxyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public IBaseResource doGet(Reference reference ) {
//        IBaseResource result = this.fhirServer.doGet( reference );
//        this.readResources.put( reference.getReference(), reference );
//        try {
//            updateProvenance();
//        } catch (FhirProxyException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public Bundle loadPage(Bundle bundle ) throws FhirProxyException {
//        return this.fhirServer.loadPage( bundle );
//    }
//
//    @Override
//    public IBaseOperationOutcome doPut(Resource iBaseResource) throws FhirProxyException {
//        IBaseOperationOutcome result = this.fhirServer.doPut( iBaseResource );
//        Reference reference = ReferenceUtil.getReference((Resource) result);
//        this.targetResources.put( reference.getReference(), reference );
//        updateProvenance();
//        return result;
//    }
//
//    @Override
//    public IBaseResource doPost(String resourceType, String resourceId, String operationName, IBaseResource body, Map<String, String> queryParams) throws FHIRException, FhirProxyException {
//        IBaseResource result = this.fhirServer.doPost(resourceType, resourceId, operationName, body, queryParams);
//        throw new FhirProxyError("check implementation");
////        this.targetResources.put( ReferenceUtil.getReference((Resource) result).getReference(), result );
////        return result;
//    }
//
//    @Override
//    public IBaseResource doPost(String resourceType, String resourceId, IBaseResource body, Map<String, String> queryParams) throws FhirProxyException {
//        IBaseResource result = this.fhirServer.doPost( resourceType, resourceId, body, queryParams );
//        throw new FhirProxyError("check implementation");
////        this.targetResources.put( ReferenceUtil.getReference((Resource) result).getReference(), result );
////        return result;
//    }
//
//    @Override
//    public IBaseResource doPost(String resourceType, IBaseResource body, Map<String, String> queryParams) throws FhirProxyException {
//        IBaseResource result = this.fhirServer.doPost( resourceType, body, queryParams );
//        throw new FhirProxyError("check implementation");
////        this.targetResources.put( ReferenceUtil.getReference((Resource) result).getReference(), result );
////        return result;
//    }
//
//    @Override
//    public IBaseResource doPost(IBaseResource body, Map<String, String> queryParams) {
//        IBaseResource result = this.fhirServer.doPost( body, queryParams );
//        throw new FhirProxyError("check implementation");
////        this.targetResources.put( ReferenceUtil.getReference((Resource) result).getReference(), result );
////        return result;
//    }
//
//    @Override
//    public MethodOutcome doPost(IBaseResource iBaseResource) throws FhirProxyException {
//        MethodOutcome methodOutcome = this.fhirServer.doPost( iBaseResource );
//        throw new FhirProxyError("check implementation");
////        this.targetResources.put( ReferenceUtil.getReference( methodOutcome.getId()), methodOutcome.getResource() ) ;
////        return methodOutcome;
//    }
//
//    @Override
//    public String doPost(String requestBody) throws FhirProxyException {
//        String methodOutcome = this.fhirServer.doPost(requestBody);
//        throw new FhirProxyError("check implementation");
////        this.targetResources.put( ReferenceUtil.getReference( methodOutcome.getId()), methodOutcome ) ;
////        return methodOutcome;
//    }
//
//    @Override
//    public FhirContext getCtx() {
//        return this.fhirServer.getCtx();
//    }
//
//    @Override
//    public IBaseResource doDelete(String resourceType, Map<String, String> queryMap) throws FhirProxyException {
//        return this.fhirServer.doDelete( resourceType, queryMap );
//    }
//
//    @Override
//    public IBaseResource doDelete(String resourceType, String resourceId, Map<String, String> queryMap) throws FhirProxyException {
//        return this.fhirServer.doDelete( resourceType, resourceId, queryMap );
//    }
//
//    @Override
//    public IBaseResource doDelete(String resourceType, String resourceId, String s2, Map<String, String> queryMap) throws FhirProxyException {
//        return this.fhirServer.doDelete( resourceType, resourceId, s2, queryMap );
//    }
//
//    @Override
//    public String getServerUrl() {
//        return this.fhirServer.getServerUrl();
//    }
//
//    @Override
//    public Bundle doGetCannonical(ResourceType resourceType, CanonicalType canonical) throws FhirProxyNotImplementedException {
//        return this.fhirServer.doGetCannonical( resourceType, canonical );
//    }
//}
