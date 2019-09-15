import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DataRequirement } from './DataRequirement'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'
import { GroupMeasureEnum } from './GroupMeasureEnum'
import { Period } from './Period'
import { Reference } from './Reference'
import { Timing } from './Timing'
import { TriggerDefinition } from './TriggerDefinition'
import { UsageContext } from './UsageContext'

export class EvidenceVariable_Characteristic      extends BackboneElement
{

   static def : string = 'EvidenceVariable_Characteristic';
   description : string ;
   definitionReference : Reference ;
   definitionCanonical : string ;
   definitionCodeableConcept : CodeableConcept ;
   definitionExpression : Expression ;
   definitionDataRequirement : DataRequirement ;
   definitionTriggerDefinition : TriggerDefinition ;
   usageContext : UsageContext [];
   exclude : boolean ;
   participantEffectiveDateTime : string ;
   participantEffectivePeriod : Period ;
   participantEffectiveDuration : string ;
   participantEffectiveTiming : Timing ;
   timeFromStart : string ;
   groupMeasure : GroupMeasureEnum ;
}
