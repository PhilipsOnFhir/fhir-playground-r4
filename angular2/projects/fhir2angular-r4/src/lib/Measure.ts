import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Measure_Group } from './Measure_Group'
import { Measure_SupplementalData } from './Measure_SupplementalData'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'
import { RelatedArtifact } from './RelatedArtifact'
import { UsageContext } from './UsageContext'

export class Measure      extends DomainResource
{

   static def : string = 'Measure';
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
   disclaimer : string ;
   scoring : CodeableConcept ;
   compositeScoring : CodeableConcept ;
   type : CodeableConcept [];
   riskAdjustment : string ;
   rateAggregation : string ;
   rationale : string ;
   clinicalRecommendationStatement : string ;
   improvementNotation : CodeableConcept ;
   definition : string [];
   guidance : string ;
   group : Measure_Group [];
   supplementalData : Measure_SupplementalData [];
}
