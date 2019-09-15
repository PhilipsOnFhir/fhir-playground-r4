import { Annotation } from './Annotation'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class RiskEvidenceSynthesis_CertaintySubcomponent      extends BackboneElement
{

   static def : string = 'RiskEvidenceSynthesis_CertaintySubcomponent';
   type : CodeableConcept ;
   rating : CodeableConcept [];
   note : Annotation [];
}
