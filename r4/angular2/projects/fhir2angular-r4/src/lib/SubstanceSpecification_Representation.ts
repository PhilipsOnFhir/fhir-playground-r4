import { Attachment } from './Attachment'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class SubstanceSpecification_Representation      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_Representation';
   type : CodeableConcept ;
   representation : string ;
   attachment : Attachment ;
}
