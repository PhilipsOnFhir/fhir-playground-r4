import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { RiskEvidenceSynthesis_PrecisionEstimate } from './RiskEvidenceSynthesis_PrecisionEstimate'

export class RiskEvidenceSynthesis_RiskEstimate      extends BackboneElement
{

   static def : string = 'RiskEvidenceSynthesis_RiskEstimate';
   description : string ;
   type : CodeableConcept ;
   value : string ;
   unitOfMeasure : CodeableConcept ;
   denominatorCount : string ;
   numeratorCount : string ;
   precisionEstimate : RiskEvidenceSynthesis_PrecisionEstimate [];
}
