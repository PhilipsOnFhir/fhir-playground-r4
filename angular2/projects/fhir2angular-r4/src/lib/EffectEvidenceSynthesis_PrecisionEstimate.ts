import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class EffectEvidenceSynthesis_PrecisionEstimate      extends BackboneElement
{

   static def : string = 'EffectEvidenceSynthesis_PrecisionEstimate';
   type : CodeableConcept ;
   level : string ;
   from : string ;
   to : string ;
}
