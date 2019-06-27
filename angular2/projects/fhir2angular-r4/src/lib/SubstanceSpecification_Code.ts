import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class SubstanceSpecification_Code      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_Code';
   code : CodeableConcept ;
   status : CodeableConcept ;
   statusDate : string ;
   comment : string ;
   source : Reference [];
}
