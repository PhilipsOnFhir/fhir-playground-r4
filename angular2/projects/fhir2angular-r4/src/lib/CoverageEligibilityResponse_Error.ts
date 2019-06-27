import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class CoverageEligibilityResponse_Error      extends BackboneElement
{

   static def : string = 'CoverageEligibilityResponse_Error';
   code : CodeableConcept ;
}
