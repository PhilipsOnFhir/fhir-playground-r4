import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { ContactDetail } from './ContactDetail'
import { DomainResource } from './DomainResource'
import { EffectEvidenceSynthesis_Certainty } from './EffectEvidenceSynthesis_Certainty'
import { EffectEvidenceSynthesis_EffectEstimate } from './EffectEvidenceSynthesis_EffectEstimate'
import { EffectEvidenceSynthesis_ResultsByExposure } from './EffectEvidenceSynthesis_ResultsByExposure'
import { EffectEvidenceSynthesis_SampleSize } from './EffectEvidenceSynthesis_SampleSize'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'
import { RelatedArtifact } from './RelatedArtifact'
import { UsageContext } from './UsageContext'

export class EffectEvidenceSynthesis      extends DomainResource
{

   static def : string = 'EffectEvidenceSynthesis';
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
   exposureAlternative : Reference ;
   outcome : Reference ;
   sampleSize : EffectEvidenceSynthesis_SampleSize ;
   resultsByExposure : EffectEvidenceSynthesis_ResultsByExposure [];
   effectEstimate : EffectEvidenceSynthesis_EffectEstimate [];
   certainty : EffectEvidenceSynthesis_Certainty [];
}
