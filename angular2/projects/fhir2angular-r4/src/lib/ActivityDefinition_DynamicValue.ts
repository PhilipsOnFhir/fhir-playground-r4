import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'

export class ActivityDefinition_DynamicValue      extends BackboneElement
{

   static def : string = 'ActivityDefinition_DynamicValue';
   path : string ;
   expression : Expression ;
}
