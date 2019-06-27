import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { EffectEvidenceSynthesis_PrecisionEstimate } from './EffectEvidenceSynthesis_PrecisionEstimate'

export class EffectEvidenceSynthesis_EffectEstimate      extends BackboneElement
{

   static def : string = 'EffectEvidenceSynthesis_EffectEstimate';
   description : string ;
   type : CodeableConcept ;
   variantState : CodeableConcept ;
   value : string ;
   unitOfMeasure : CodeableConcept ;
   precisionEstimate : EffectEvidenceSynthesis_PrecisionEstimate [];
}
