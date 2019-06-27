import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Dosage } from './Dosage'
import { Identifier } from './Identifier'
import { MedicationDispense_Performer } from './MedicationDispense_Performer'
import { MedicationDispense_Substitution } from './MedicationDispense_Substitution'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class MedicationDispense      extends DomainResource
{

   static def : string = 'MedicationDispense';
   identifier : Identifier [];
   partOf : Reference [];
   status : string ;
   statusReasonCodeableConcept : CodeableConcept ;
   statusReasonReference : Reference ;
   category : CodeableConcept ;
   medicationCodeableConcept : CodeableConcept ;
   medicationReference : Reference ;
   subject : Reference ;
   context : Reference ;
   supportingInformation : Reference [];
   performer : MedicationDispense_Performer [];
   location : Reference ;
   authorizingPrescription : Reference [];
   type : CodeableConcept ;
   quantity : Quantity ;
   daysSupply : Quantity ;
   whenPrepared : string ;
   whenHandedOver : string ;
   destination : Reference ;
   receiver : Reference [];
   note : Annotation [];
   dosageInstruction : Dosage [];
   substitution : MedicationDispense_Substitution ;
   detectedIssue : Reference [];
   eventHistory : Reference [];
}
