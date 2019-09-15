import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class CoverageEligibilityRequest_Insurance      extends BackboneElement
{

   static def : string = 'CoverageEligibilityRequest_Insurance';
   focal : boolean ;
   coverage : Reference ;
   businessArrangement : string ;
}
