import {DomainResource} from "../../../../../fhir2angular-r4/src/lib/DomainResource";
import {Patient} from "../../../../../fhir2angular-r4/src/lib/Patient";
import {PatientUtil} from "./patient-util";
import {ImagingStudy} from "../../../../../fhir2angular-r4/src/lib/ImagingStudy";

export class ResourceUtil {

  static getSummary( domainResource: DomainResource): string {
    let summary: string = "Unknown";

    switch ( domainResource.resourceType) {
      case Patient.def:
        summary = PatientUtil.getPreferredName( domainResource as Patient);
        break;
      default:
        summary = domainResource.resourceType+"-"+domainResource.id;
    }
    return summary;
  }
}
