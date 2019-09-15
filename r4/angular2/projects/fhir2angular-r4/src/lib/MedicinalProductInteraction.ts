import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { MedicinalProductInteraction_Interactant } from './MedicinalProductInteraction_Interactant'
import { Reference } from './Reference'

export class MedicinalProductInteraction      extends DomainResource
{

   static def : string = 'MedicinalProductInteraction';
   subject : Reference [];
   description : string ;
   interactant : MedicinalProductInteraction_Interactant [];
   type : CodeableConcept ;
   effect : CodeableConcept ;
   incidence : CodeableConcept ;
   management : CodeableConcept ;
}
