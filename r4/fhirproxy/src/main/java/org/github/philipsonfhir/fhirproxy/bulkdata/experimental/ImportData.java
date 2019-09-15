package org.github.philipsonfhir.fhirproxy.bulkdata.experimental;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
class ImportData {
    String importFormat;
    String inputSource;
    List<Input> input;
    StorageDetail storageDetail;



}
