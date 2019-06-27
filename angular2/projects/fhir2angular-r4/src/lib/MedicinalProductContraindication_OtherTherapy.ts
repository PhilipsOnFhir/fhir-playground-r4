import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class MedicinalProductContraindication_OtherTherapy      extends BackboneElement
{

   static def : string = 'MedicinalProductContraindication_OtherTherapy';
   therapyRelationshipType : CodeableConcept ;
   medicationCodeableConcept : CodeableConcept ;
   medicationReference : Reference ;
}
