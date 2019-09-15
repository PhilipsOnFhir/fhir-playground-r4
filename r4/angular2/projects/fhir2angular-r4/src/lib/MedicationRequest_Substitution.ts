import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class MedicationRequest_Substitution      extends BackboneElement
{

   static def : string = 'MedicationRequest_Substitution';
   allowedBoolean : boolean ;
   allowedCodeableConcept : CodeableConcept ;
   reason : CodeableConcept ;
}
