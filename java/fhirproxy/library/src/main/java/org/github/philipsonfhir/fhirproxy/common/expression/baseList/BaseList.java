package org.github.philipsonfhir.fhirproxy.common.expression.baseList;

import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Property;

import java.util.List;

public class BaseList extends Base {

    List<Base> baseList;

    public BaseList(List<Base> values) {
        baseList =values;
    }

    @Override
    public String fhirType() {
        return "BaseList";
    }

    @Override
    protected void listChildren(List<Property> result) {
        throw new Error();
    }

    @Override
    public String getIdBase() {
        throw new Error();
    }

    @Override
    public void setIdBase(String value) {
        throw new Error();
    }

    public List<Base> getList() {
        return baseList;
    }
}
