import { Annotation } from './Annotation'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { RiskEvidenceSynthesis_CertaintySubcomponent } from './RiskEvidenceSynthesis_CertaintySubcomponent'

export class RiskEvidenceSynthesis_Certainty      extends BackboneElement
{

   static def : string = 'RiskEvidenceSynthesis_Certainty';
   rating : CodeableConcept [];
   note : Annotation [];
   certaintySubcomponent : RiskEvidenceSynthesis_CertaintySubcomponent [];
}
