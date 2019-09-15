import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class Claim_Procedure      extends BackboneElement
{

   static def : string = 'Claim_Procedure';
   sequence : string ;
   type : CodeableConcept [];
   date : string ;
   procedureCodeableConcept : CodeableConcept ;
   procedureReference : Reference ;
   udi : Reference [];
}
