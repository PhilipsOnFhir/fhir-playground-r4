import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { EncounterLocationStatusEnum } from './EncounterLocationStatusEnum'
import { Period } from './Period'
import { Reference } from './Reference'

export class Encounter_Location      extends BackboneElement
{

   static def : string = 'Encounter_Location';
   location : Reference ;
   status : EncounterLocationStatusEnum ;
   physicalType : CodeableConcept ;
   period : Period ;
}
