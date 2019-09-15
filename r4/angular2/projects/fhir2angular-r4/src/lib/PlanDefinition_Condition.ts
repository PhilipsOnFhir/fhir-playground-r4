import { ActionConditionKindEnum } from './ActionConditionKindEnum'
import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'

export class PlanDefinition_Condition      extends BackboneElement
{

   static def : string = 'PlanDefinition_Condition';
   kind : ActionConditionKindEnum ;
   expression : Expression ;
}
