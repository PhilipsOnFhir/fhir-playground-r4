import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Period } from './Period'
import { Reference } from './Reference'

export class DocumentReference_Context      extends BackboneElement
{

   static def : string = 'DocumentReference_Context';
   encounter : Reference [];
   event : CodeableConcept [];
   period : Period ;
   facilityType : CodeableConcept ;
   practiceSetting : CodeableConcept ;
   sourcePatientInfo : Reference ;
   related : Reference [];
}
