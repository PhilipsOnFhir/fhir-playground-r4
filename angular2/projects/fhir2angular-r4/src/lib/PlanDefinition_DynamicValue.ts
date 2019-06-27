import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'

export class PlanDefinition_DynamicValue      extends BackboneElement
{

   static def : string = 'PlanDefinition_DynamicValue';
   path : string ;
   expression : Expression ;
}
