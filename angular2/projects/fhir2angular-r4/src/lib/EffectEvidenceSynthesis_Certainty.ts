import { Annotation } from './Annotation'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { EffectEvidenceSynthesis_CertaintySubcomponent } from './EffectEvidenceSynthesis_CertaintySubcomponent'

export class EffectEvidenceSynthesis_Certainty      extends BackboneElement
{

   static def : string = 'EffectEvidenceSynthesis_Certainty';
   rating : CodeableConcept [];
   note : Annotation [];
   certaintySubcomponent : EffectEvidenceSynthesis_CertaintySubcomponent [];
}
