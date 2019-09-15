import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Period } from './Period'

export class Coverage_Exception      extends BackboneElement
{

   static def : string = 'Coverage_Exception';
   type : CodeableConcept ;
   period : Period ;
}
