import { DataRequirement } from './DataRequirement'
import { DomainResource } from './DomainResource'
import { Element } from './Element'
import { Expression } from './Expression'
import { Reference } from './Reference'
import { Timing } from './Timing'
import { TriggerTypeEnum } from './TriggerTypeEnum'

export class TriggerDefinition      extends Element
{

   static def : string = 'TriggerDefinition';
   type : TriggerTypeEnum ;
   name : string ;
   timingTiming : Timing ;
   timingReference : Reference ;
   timingDate : string ;
   timingDateTime : string ;
   data : DataRequirement [];
   condition : Expression ;
}
