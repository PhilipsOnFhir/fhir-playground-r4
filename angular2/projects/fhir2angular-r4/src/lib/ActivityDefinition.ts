import { ActivityDefinition_DynamicValue } from './ActivityDefinition_DynamicValue'
import { ActivityDefinition_Participant } from './ActivityDefinition_Participant'
import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { Dosage } from './Dosage'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Quantity } from './Quantity'
import { Range } from './Range'
import { Reference } from './Reference'
import { RelatedArtifact } from './RelatedArtifact'
import { RequestIntentEnum } from './RequestIntentEnum'
import { RequestPriorityEnum } from './RequestPriorityEnum'
import { RequestResourceTypeEnum } from './RequestResourceTypeEnum'
import { Timing } from './Timing'
import { UsageContext } from './UsageContext'

export class ActivityDefinition      extends DomainResource
{

   static def : string = 'ActivityDefinition';
   url : string ;
   identifier : Identifier [];
   version : string ;
   name : string ;
   title : string ;
   subtitle : string ;
   status : PublicationStatusEnum ;
   experimental : boolean ;
   subjectCodeableConcept : CodeableConcept ;
   subjectReference : Reference ;
   date : string ;
   publisher : string ;
   contact : ContactDetail [];
   description : string ;
   useContext : UsageContext [];
   jurisdiction : CodeableConcept [];
   purpose : string ;
   usage : string ;
   copyright : string ;
   approvalDate : string ;
   lastReviewDate : string ;
   effectivePeriod : Period ;
   topic : CodeableConcept [];
   author : ContactDetail [];
   editor : ContactDetail [];
   reviewer : ContactDetail [];
   endorser : ContactDetail [];
   relatedArtifact : RelatedArtifact [];
   library : string [];
   kind : RequestResourceTypeEnum ;
   profile : string ;
   code : CodeableConcept ;
   intent : RequestIntentEnum ;
   priority : RequestPriorityEnum ;
   doNotPerform : boolean ;
   timingTiming : Timing ;
   timingDateTime : string ;
   timingAge : string ;
   timingPeriod : Period ;
   timingRange : Range ;
   timingDuration : string ;
   location : Reference ;
   participant : ActivityDefinition_Participant [];
   productReference : Reference ;
   productCodeableConcept : CodeableConcept ;
   quantity : Quantity ;
   dosage : Dosage [];
   bodySite : CodeableConcept [];
   specimenRequirement : Reference [];
   observationRequirement : Reference [];
   observationResultRequirement : Reference [];
   transform : string ;
   dynamicValue : ActivityDefinition_DynamicValue [];
}
