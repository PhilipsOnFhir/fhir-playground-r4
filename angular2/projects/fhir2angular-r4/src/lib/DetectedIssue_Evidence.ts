import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class DetectedIssue_Evidence      extends BackboneElement
{

   static def : string = 'DetectedIssue_Evidence';
   code : CodeableConcept [];
   detail : Reference [];
}
