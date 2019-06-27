import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class MedicationKnowledge_Schedule      extends BackboneElement
{

   static def : string = 'MedicationKnowledge_Schedule';
   schedule : CodeableConcept ;
}
