import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DataRequirement } from './DataRequirement'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'
import { GroupMeasureEnum } from './GroupMeasureEnum'
import { Period } from './Period'
import { Timing } from './Timing'
import { UsageContext } from './UsageContext'

export class ResearchElementDefinition_Characteristic      extends BackboneElement
{

   static def : string = 'ResearchElementDefinition_Characteristic';
   definitionCodeableConcept : CodeableConcept ;
   definitionCanonical : string ;
   definitionExpression : Expression ;
   definitionDataRequirement : DataRequirement ;
   usageContext : UsageContext [];
   exclude : boolean ;
   unitOfMeasure : CodeableConcept ;
   studyEffectiveDescription : string ;
   studyEffectiveDateTime : string ;
   studyEffectivePeriod : Period ;
   studyEffectiveDuration : string ;
   studyEffectiveTiming : Timing ;
   studyEffectiveTimeFromStart : string ;
   studyEffectiveGroupMeasure : GroupMeasureEnum ;
   participantEffectiveDescription : string ;
   participantEffectiveDateTime : string ;
   participantEffectivePeriod : Period ;
   participantEffectiveDuration : string ;
   participantEffectiveTiming : Timing ;
   participantEffectiveTimeFromStart : string ;
   participantEffectiveGroupMeasure : GroupMeasureEnum ;
}
