package org.github.philipsonfhir.fhirproxy.common.util;

import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Questionnaire;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExtensionUtil {
    public static final String QUESTIONNAIRE_OBSERVATION_LINK_PERIOD_EXTENSION = "http://hl7.org/fhir/uv/sdc/StructureDefinition/questionnaire-observationLinkPeriod";
    public static final String QUESTIONNAIRE_UNIT_EXTENSION = "http://hl7.org/fhir/extension-questionnaire-unit.html";
    public static final String QUESTIONNAIRE_INITIAL_EXPRESSION_EXTENSION = "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-initialExpression";
    public static final String QUESTIONNAIRE_CALCULATED_EXPRESSION_EXTENSION = "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-calculatedExpression";
    public static final String QUESTIONNAIRE_LAUNCHCONTEXT = "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-launchContext";
    public static final String QUESTIONNAIRE_ENABLE_WHEN_EXTENSION = "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-enableWhenExpression";
    public static final String QUESTIONNAIRE_CONSTRAINT_EXTENSION = "http://hl7.org/fhir/StructureDefinition/questionnaire-constraint";
    public static final String QUESTIONNAIRE_VARIABLE_EXTENSION = "http://hl7.org/fhir/StructureDefinition/variable";
    public static final String QUESTIONNAIRE_ITEM_CONTEXT_EXTENSION = "http://hl7.org/fhir/uv/sdc/StructureDefinition/sdc-questionnaire-itemContext";

    public static boolean hasExtension(Questionnaire.QuestionnaireItemComponent questionnaireItem, String url){
        return extensionOpt(questionnaireItem, ExtensionUtil.QUESTIONNAIRE_INITIAL_EXPRESSION_EXTENSION).isPresent();
    }

    public static Extension getExtension(Questionnaire.QuestionnaireItemComponent questionnaireItem, String url ) {
        return extensionOpt(questionnaireItem, url ).get();
    }

    public static List<Extension> getExtensions(List<Extension> extensions, String url ) {
        return extensions.stream()
                .filter( extension -> extension.getUrl().equals( url ))
                .collect(Collectors.toList());
    }

    public static Optional<Extension> extensionOpt(BackboneElement questionnaireItem, String url ){
        return extensionOpt( questionnaireItem.getExtension(), url );
    }

    public static Optional<Extension> extensionOpt(List<Extension> extensions, String url){
        Optional<Extension> ExtensionOpt = extensions.stream()
                .filter( extension -> extension.getUrl().equals( url ))
                .findFirst();
        return ExtensionOpt;
    }


}
