import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'
import { RelatedArtifact } from './RelatedArtifact'
import { UsageContext } from './UsageContext'

export class Evidence      extends DomainResource
{

   static def : string = 'Evidence';
   url : string ;
   identifier : Identifier [];
   version : string ;
   name : string ;
   title : string ;
   shortTitle : string ;
   subtitle : string ;
   status : PublicationStatusEnum ;
   date : string ;
   publisher : string ;
   contact : ContactDetail [];
   description : string ;
   note : Annotation [];
   useContext : UsageContext [];
   jurisdiction : CodeableConcept [];
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
   exposureBackground : Reference ;
   exposureVariant : Reference [];
   outcome : Reference [];
}
