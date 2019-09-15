import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class Coverage_Class      extends BackboneElement
{

   static def : string = 'Coverage_Class';
   type : CodeableConcept ;
   value : string ;
   name : string ;
}
