import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class CoverageEligibilityRequest_Diagnosis      extends BackboneElement
{

   static def : string = 'CoverageEligibilityRequest_Diagnosis';
   diagnosisCodeableConcept : CodeableConcept ;
   diagnosisReference : Reference ;
}
