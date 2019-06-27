import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class Contract_Party      extends BackboneElement
{

   static def : string = 'Contract_Party';
   reference : Reference [];
   role : CodeableConcept ;
}
