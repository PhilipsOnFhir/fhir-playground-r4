import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { CoverageEligibilityRequest_Diagnosis } from './CoverageEligibilityRequest_Diagnosis'
import { DomainResource } from './DomainResource'
import { Money } from './Money'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class CoverageEligibilityRequest_Item      extends BackboneElement
{

   static def : string = 'CoverageEligibilityRequest_Item';
   supportingInfoSequence : string [];
   category : CodeableConcept ;
   productOrService : CodeableConcept ;
   modifier : CodeableConcept [];
   provider : Reference ;
   quantity : Quantity ;
   unitPrice : Money ;
   facility : Reference ;
   diagnosis : CoverageEligibilityRequest_Diagnosis [];
   detail : Reference [];
}
