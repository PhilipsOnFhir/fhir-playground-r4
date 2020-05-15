package org.github.philipsonfhir.smartsuite;

public class Prefix {
    public static final String BASE               = "/api";
    public static final String CONTEXTSYNC        = BASE+"/sync";
    public static final String TOPIC              = CONTEXTSYNC+"/topic" ;
    public static final String FHIRCAST           = CONTEXTSYNC+"/fhircast" ;
    public static final String FHIRCAST_WEBSOCKET = CONTEXTSYNC +"/websocket";
    public static final String LAUNCH             = CONTEXTSYNC+"/launch" ;
}
