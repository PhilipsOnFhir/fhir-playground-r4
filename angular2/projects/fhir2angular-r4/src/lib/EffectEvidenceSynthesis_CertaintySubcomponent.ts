import { Annotation } from './Annotation'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class EffectEvidenceSynthesis_CertaintySubcomponent      extends BackboneElement
{

   static def : string = 'EffectEvidenceSynthesis_CertaintySubcomponent';
   type : CodeableConcept ;
   rating : CodeableConcept [];
   note : Annotation [];
}
