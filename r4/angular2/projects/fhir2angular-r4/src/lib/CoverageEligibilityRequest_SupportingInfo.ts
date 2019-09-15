import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class CoverageEligibilityRequest_SupportingInfo      extends BackboneElement
{

   static def : string = 'CoverageEligibilityRequest_SupportingInfo';
   sequence : string ;
   information : Reference ;
   appliesToAll : boolean ;
}
