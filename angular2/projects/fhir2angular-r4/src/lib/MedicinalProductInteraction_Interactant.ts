import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class MedicinalProductInteraction_Interactant      extends BackboneElement
{

   static def : string = 'MedicinalProductInteraction_Interactant';
   itemReference : Reference ;
   itemCodeableConcept : CodeableConcept ;
}
