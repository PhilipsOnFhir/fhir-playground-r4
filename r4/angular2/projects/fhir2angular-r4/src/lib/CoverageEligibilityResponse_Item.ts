import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { CoverageEligibilityResponse_Benefit } from './CoverageEligibilityResponse_Benefit'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class CoverageEligibilityResponse_Item      extends BackboneElement
{

   static def : string = 'CoverageEligibilityResponse_Item';
   category : CodeableConcept ;
   productOrService : CodeableConcept ;
   modifier : CodeableConcept [];
   provider : Reference ;
   excluded : boolean ;
   name : string ;
   description : string ;
   network : CodeableConcept ;
   unit : CodeableConcept ;
   term : CodeableConcept ;
   benefit : CoverageEligibilityResponse_Benefit [];
   authorizationRequired : boolean ;
   authorizationSupporting : CodeableConcept [];
   authorizationUrl : string ;
}
