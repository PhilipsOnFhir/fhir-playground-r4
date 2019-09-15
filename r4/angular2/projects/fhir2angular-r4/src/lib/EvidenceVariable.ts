import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { EvidenceVariableTypeEnum } from './EvidenceVariableTypeEnum'
import { EvidenceVariable_Characteristic } from './EvidenceVariable_Characteristic'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { RelatedArtifact } from './RelatedArtifact'
import { UsageContext } from './UsageContext'

export class EvidenceVariable      extends DomainResource
{

   static def : string = 'EvidenceVariable';
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
   type : EvidenceVariableTypeEnum ;
   characteristic : EvidenceVariable_Characteristic [];
}
