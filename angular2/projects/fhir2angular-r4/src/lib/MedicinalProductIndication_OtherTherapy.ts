import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class MedicinalProductIndication_OtherTherapy      extends BackboneElement
{

   static def : string = 'MedicinalProductIndication_OtherTherapy';
   therapyRelationshipType : CodeableConcept ;
   medicationCodeableConcept : CodeableConcept ;
   medicationReference : Reference ;
}
