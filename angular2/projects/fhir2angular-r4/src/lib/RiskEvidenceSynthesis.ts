import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'
import { RelatedArtifact } from './RelatedArtifact'
import { RiskEvidenceSynthesis_Certainty } from './RiskEvidenceSynthesis_Certainty'
import { RiskEvidenceSynthesis_RiskEstimate } from './RiskEvidenceSynthesis_RiskEstimate'
import { RiskEvidenceSynthesis_SampleSize } from './RiskEvidenceSynthesis_SampleSize'
import { UsageContext } from './UsageContext'

export class RiskEvidenceSynthesis      extends DomainResource
{

   static def : string = 'RiskEvidenceSynthesis';
   url : string ;
   identifier : Identifier [];
   version : string ;
   name : string ;
   title : string ;
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
   synthesisType : CodeableConcept ;
   studyType : CodeableConcept ;
   population : Reference ;
   exposure : Reference ;
   outcome : Reference ;
   sampleSize : RiskEvidenceSynthesis_SampleSize ;
   riskEstimate : RiskEvidenceSynthesis_RiskEstimate ;
   certainty : RiskEvidenceSynthesis_Certainty [];
}
