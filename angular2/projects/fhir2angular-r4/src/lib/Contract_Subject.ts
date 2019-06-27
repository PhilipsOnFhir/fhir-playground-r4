import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class Contract_Subject      extends BackboneElement
{

   static def : string = 'Contract_Subject';
   reference : Reference [];
   role : CodeableConcept ;
}
