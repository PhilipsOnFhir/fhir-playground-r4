package org.github.philipsonfhir.fhirproxy.common.util;

public class BasePath {
    private final String[] templateElements;
    private final String[] elements;

    public BasePath(String[] templateElements, String[] elements) {
        this.templateElements = templateElements;
        this.elements = elements;
    }

    public String getPathElement(String templateElement ) {
        String result = null;
        for ( int i=0; i<Math.min(templateElement.length(),elements.length );i++){
            if ( templateElements[i].equals( "{"+templateElement+"}") ){
                result = elements[i];
            }
        }
        return result;
    }
}
