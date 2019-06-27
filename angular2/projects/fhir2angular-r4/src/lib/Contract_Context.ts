import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class Contract_Context      extends BackboneElement
{

   static def : string = 'Contract_Context';
   reference : Reference ;
   code : CodeableConcept [];
   text : string ;
}
