import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { MedicationStatusCodesEnum } from './MedicationStatusCodesEnum'
import { Medication_Batch } from './Medication_Batch'
import { Medication_Ingredient } from './Medication_Ingredient'
import { Ratio } from './Ratio'
import { Reference } from './Reference'

export class Medication      extends DomainResource
{

   static def : string = 'Medication';
   identifier : Identifier [];
   code : CodeableConcept ;
   status : MedicationStatusCodesEnum ;
   manufacturer : Reference ;
   form : CodeableConcept ;
   amount : Ratio ;
   ingredient : Medication_Ingredient [];
   batch : Medication_Batch ;
}
