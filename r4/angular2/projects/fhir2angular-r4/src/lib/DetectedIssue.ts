import { CodeableConcept } from './CodeableConcept'
import { DetectedIssueSeverityEnum } from './DetectedIssueSeverityEnum'
import { DetectedIssue_Evidence } from './DetectedIssue_Evidence'
import { DetectedIssue_Mitigation } from './DetectedIssue_Mitigation'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { ObservationStatusEnum } from './ObservationStatusEnum'
import { Period } from './Period'
import { Reference } from './Reference'

export class DetectedIssue      extends DomainResource
{

   static def : string = 'DetectedIssue';
   identifier : Identifier [];
   status : ObservationStatusEnum ;
   code : CodeableConcept ;
   severity : DetectedIssueSeverityEnum ;
   patient : Reference ;
   identifiedDateTime : string ;
   identifiedPeriod : Period ;
   author : Reference ;
   implicated : Reference [];
   evidence : DetectedIssue_Evidence [];
   detail : string ;
   reference : string ;
   mitigation : DetectedIssue_Mitigation [];
}
