import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Money } from './Money'

export class CoverageEligibilityResponse_Benefit      extends BackboneElement
{

   static def : string = 'CoverageEligibilityResponse_Benefit';
   type : CodeableConcept ;
   allowedUnsignedInt : string ;
   allowedString : string ;
   allowedMoney : Money ;
   usedUnsignedInt : string ;
   usedString : string ;
   usedMoney : Money ;
}
