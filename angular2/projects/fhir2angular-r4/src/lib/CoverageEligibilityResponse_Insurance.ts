import { BackboneElement } from './BackboneElement'
import { CoverageEligibilityResponse_Item } from './CoverageEligibilityResponse_Item'
import { DomainResource } from './DomainResource'
import { Period } from './Period'
import { Reference } from './Reference'

export class CoverageEligibilityResponse_Insurance      extends BackboneElement
{

   static def : string = 'CoverageEligibilityResponse_Insurance';
   coverage : Reference ;
   inforce : boolean ;
   benefitPeriod : Period ;
   item : CoverageEligibilityResponse_Item [];
}
