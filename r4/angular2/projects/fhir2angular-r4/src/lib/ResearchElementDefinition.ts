import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'
import { RelatedArtifact } from './RelatedArtifact'
import { ResearchElementDefinition_Characteristic } from './ResearchElementDefinition_Characteristic'
import { ResearchElementTypeEnum } from './ResearchElementTypeEnum'
import { UsageContext } from './UsageContext'
import { VariableTypeEnum } from './VariableTypeEnum'

export class ResearchElementDefinition      extends DomainResource
{

   static def : string = 'ResearchElementDefinition';
   url : string ;
   identifier : Identifier [];
   version : string ;
   name : string ;
   title : string ;
   shortTitle : string ;
   subtitle : string ;
   status : PublicationStatusEnum ;
   experimental : boolean ;
   subjectCodeableConcept : CodeableConcept ;
   subjectReference : Reference ;
   date : string ;
   publisher : string ;
   contact : ContactDetail [];
   description : string ;
   comment : string [];
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
   type : ResearchElementTypeEnum ;
   variableType : VariableTypeEnum ;
   characteristic : ResearchElementDefinition_Characteristic [];
}
