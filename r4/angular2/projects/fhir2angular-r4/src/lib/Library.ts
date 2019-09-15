import { Attachment } from './Attachment'
import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DataRequirement } from './DataRequirement'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { ParameterDefinition } from './ParameterDefinition'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'
import { RelatedArtifact } from './RelatedArtifact'
import { UsageContext } from './UsageContext'

export class Library      extends DomainResource
{

   static def : string = 'Library';
   url : string ;
   identifier : Identifier [];
   version : string ;
   name : string ;
   title : string ;
   subtitle : string ;
   status : PublicationStatusEnum ;
   experimental : boolean ;
   type : CodeableConcept ;
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
   parameter : ParameterDefinition [];
   dataRequirement : DataRequirement [];
   content : Attachment [];
}
