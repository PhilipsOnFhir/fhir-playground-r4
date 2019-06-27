import { ActionConditionKindEnum } from './ActionConditionKindEnum'
import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'

export class RequestGroup_Condition      extends BackboneElement
{

   static def : string = 'RequestGroup_Condition';
   kind : ActionConditionKindEnum ;
   expression : Expression ;
}
