package org.github.philipsonfhir.fhirproxy.common;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;

public class FhirValueSetter {
    public static Base setProperty(Base dest, String element, Object obj ) throws FHIRException, FhirProxyException {
        Base v = FhirValueSetter.getBaseValue(obj);
        String cleanedElement = element.replace("\\s"," ");

        if (dest instanceof Resource && element.startsWith(((Resource) dest).getResourceType().name())
        ) {
            cleanedElement = element.replace( ((Resource) dest).getResourceType().name()+".","");
        }

        if ( cleanedElement.startsWith("(")){
            cleanedElement = cleanedElement.substring( 1 );
        }

        // check direct or hasChildern.
        Base result = dest;
        if ( cleanedElement.contains(".")){
            // has childern
            String first = cleanedElement.substring(0, cleanedElement.indexOf("."));
            String remaining = cleanedElement.substring(cleanedElement.indexOf(".")+1);

            if ( first.endsWith(")") && !first.endsWith("()") && !first.endsWith("( )")) {
                first = first.substring( 0, first.length()-1 );
            }

            Base[] firstElement = null;
            int index = 0;
            checkFunctions(first);

            if ( first.contains("[") ){
                if ( !first.contains("]")){
                    throw new FhirProxyException("parsing path "+element+ " contains [ but no ]");
                }
                String firstAr = first.substring( 0, first.indexOf("["));
                String indexStr = first.substring(first.indexOf("[")+1);
                indexStr = indexStr.substring(0, indexStr.indexOf("]"));
                index = Integer.parseInt( indexStr );
                first = firstAr;
                firstElement = dest.getProperty( firstAr.hashCode(), firstAr, true );
            } else {
                firstElement = dest.getProperty( first.hashCode(), first, false );
            }

            if ( firstElement==null ) {
                firstElement = checkAndProcessAs(dest, first, firstElement);
            }
            if ( firstElement==null ) {
                firstElement = checkAndProcessValueX(dest, first, firstElement);
            }

            if ( firstElement.length==0){
                // create object
                createAndAddInstance( dest, first, firstElement );
//                dest.makeProperty( first.hashCode(), first );
                firstElement = dest.getProperty( first.hashCode(), first, true );
            }
            if ( firstElement.length==0 ){
                throw new FhirProxyException("Cannot create "+first);
            }
            if ( index >= firstElement.length ) {
                int number = index-firstElement.length+1;
                for (int i = 0; i < number; i++) {
                    createAndAddInstance( dest, first, firstElement );
                }
                firstElement = dest.getProperty( first.hashCode(), first, true );

//                throw new FhirProxyException( "index "+ index + " is larger than lenght of array "+element + " with length "+ firstElement.length );
            }
            result = setProperty( firstElement[index], remaining, v );
        } else{
            checkFunctions(element);
            if ( v !=null ){
                result = dest.setProperty( element.hashCode(), element, v );
            }
        }

        return result;
    }

    private static void createAndAddInstance(Base dest, String first, Base[] firstElement) throws FhirProxyException {
        int firstElementSize = ( firstElement==null ? 0 : firstElement.length );
        firstElement = checkAndProcessAs(dest, first, firstElement);
        if ( firstElement==null || firstElement.length == firstElementSize ) {
            firstElement = checkAndProcessValueX(dest, first, firstElement);
        }
        if ( firstElement==null || firstElement.length == firstElementSize ) {
            dest.makeProperty( first.hashCode(), first );
        }
    }

    private static Base[] checkAndProcessAs(Base dest, String first, Base[] firstElement) throws FhirProxyException {
        if ( first.contains(" as ")){
            System.out.println("process "+first);
            String propertyName = first.substring(0, first.indexOf(" as"));
            firstElement = dest.getProperty( propertyName.hashCode(), propertyName, false );
            if ( firstElement == null || firstElement.length==0) {
                String typeName = first.substring(first.indexOf("as ") + 3).trim();
                createAndSetType(dest, propertyName, typeName);
                firstElement = dest.getProperty(propertyName.hashCode(), propertyName, true);
            }
        }
        return firstElement;
    }

    private static Base[] checkAndProcessValueX(Base dest, String first, Base[] firstElement) throws FhirProxyException {
        // check for value[x]
        for ( Property property : dest.children() ){
            String propertyName = property.getName();

            if ( property.getName().endsWith("[x]") && first.startsWith(propertyName.substring(0,propertyName.length()-3)) ){
                // value[x] type with multiple typers
                propertyName = propertyName.substring(0,propertyName.length()-3);
                String typeCode = property.getTypeCode();
                String[] types = typeCode.split("\\|");
                //create element of the correct type and add it.
                for ( String type: types ){
                    if ( (propertyName+type).equals(first) ){
                        createAndSetType(dest, propertyName, type);
//                                    TypesUtilities.wildcardTypes();
                    }
                }
                // get firstElement

                firstElement = dest.getProperty( propertyName.hashCode(), propertyName, true );
            }
        }
        return firstElement;
    }

    private static void createAndSetType(Base dest, String propertyName, String type) throws FhirProxyException {
        System.out.println("create "+type);
        try {
            Class<?> clazz = Class.forName("org.hl7.fhir.r4.model."+type);
            Object ni = clazz.getDeclaredConstructor().newInstance();
            dest.setProperty( propertyName.hashCode(), propertyName, (Base) ni);
//                                        firstElement = (Base[]) ni;
        } catch ( Exception e) {
            e.printStackTrace();
            throw new FhirProxyException(e);
        }
    }

    private static void checkFunctions(String first) throws FhirProxyException {
        if ( first.startsWith("resolve()")){
            throw new FhirProxyException("resolve() is not supported");
        }
        if ( first.startsWith("extension(")){
            throw new FhirProxyException("extension() is not supported");
        }
        if ( first.startsWith("ofType(")){
            throw new FhirProxyException("ofType() is not supported");
        }
    }

    public static Base getBaseValue(Object value) throws FHIRException {
        if ( value instanceof Base) {
            return (Base) value;
        } else if ( value instanceof String){
            return new StringType((String)value);
        } else if ( value instanceof Boolean){
            return new BooleanType((Boolean) value);
        } else if ( value instanceof Integer){
            return new IntegerType((Integer) value);
        } else if ( value instanceof Float  ){
            return new DecimalType((Float) value);
        } else if ( value instanceof Double ){
            return new DecimalType((Double) value);
        } else if ( value == null ){
            return null;
        }
        throw new FHIRException("Could not cast "+value.getClass()+" to Base.");
    }
}
