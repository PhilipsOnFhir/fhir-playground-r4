import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class RiskEvidenceSynthesis_PrecisionEstimate      extends BackboneElement
{

   static def : string = 'RiskEvidenceSynthesis_PrecisionEstimate';
   type : CodeableConcept ;
   level : string ;
   from : string ;
   to : string ;
}
