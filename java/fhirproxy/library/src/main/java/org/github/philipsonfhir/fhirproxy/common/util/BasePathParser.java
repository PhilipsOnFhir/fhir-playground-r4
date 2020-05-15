package org.github.philipsonfhir.fhirproxy.common.util;

public class BasePathParser {
    private final String template;
    private final String[] templateElements;

    public BasePathParser(String template ) {
        this.template = template;
        this.templateElements = template.replace("//", "/").split("/");
    }

    public BasePath parse(String path) {
        String[] elements = path.replace("//", "/").split("/");
        BasePath basePath = new BasePath( templateElements, elements );
        return basePath;
    }
}
