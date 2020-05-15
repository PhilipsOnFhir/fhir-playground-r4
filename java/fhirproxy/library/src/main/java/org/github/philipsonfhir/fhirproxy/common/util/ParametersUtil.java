package org.github.philipsonfhir.fhirproxy.common.util;

import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParametersUtil {
//    Base getParameter(String name ){
//        Type result = getParameter().stream()
//                .filter(parametersParameterComponent -> parametersParameterComponent.getName().equals(name))
//                .findFirst().get().getValue();
//        return result;
//    }

    public static Parameters.ParametersParameterComponent getParameter(Parameters myParameters, String name) {
        Optional<Parameters.ParametersParameterComponent> opt = myParameters.getParameter().stream()
                .filter(parametersParameterComponent -> parametersParameterComponent.getName().equals(name))
                .findFirst();
        if ( opt.isPresent() ){
            return opt.get();
        }
        return null;
    }

    public static List<Parameters.ParametersParameterComponent> getParameters(Parameters myParameters, String name) {
        List<Parameters.ParametersParameterComponent> list = myParameters.getParameter().stream()
                .filter(parametersParameterComponent -> parametersParameterComponent.getName().equals(name))
                .collect(Collectors.toList());

        return list;
    }

    public static boolean holdsParameter(Parameters myParameters, String name) {
        Optional<Parameters.ParametersParameterComponent> result = myParameters.getParameter().stream()
                .filter(parametersParameterComponent -> parametersParameterComponent.getName().equals(name))
                .findFirst();
        return result.isPresent();
    }

    public static Type getParameterValue(Parameters parameters, String name) {
        if ( holdsParameter(parameters, name)){
            return getParameter(parameters, name).getValue();
        }
        return null;
    }

    public static Resource getParameterResource(Parameters parameters, String name) {
        if ( holdsParameter(parameters, name)){
            return getParameter(parameters, name).getResource();
        }
        return null;
    }

    public static void addReferenceParameter(Parameters parameters, String name, Map<String, String> queryMap) {
        if ( queryMap.containsKey(name)){
            parameters.addParameter( new Parameters.ParametersParameterComponent()
                    .setName(name)
                    .setValue(new Reference().setReference(queryMap.get(name)))
            );
        }


    }
}
