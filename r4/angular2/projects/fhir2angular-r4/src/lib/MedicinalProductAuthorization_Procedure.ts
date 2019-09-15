import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'

export class MedicinalProductAuthorization_Procedure      extends BackboneElement
{

   static def : string = 'MedicinalProductAuthorization_Procedure';
   identifier : Identifier ;
   type : CodeableConcept ;
   datePeriod : Period ;
   dateDateTime : string ;
   application : MedicinalProductAuthorization_Procedure [];
}
