import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { Contract_Answer } from './Contract_Answer'
import { Contract_Party } from './Contract_Party'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Reference } from './Reference'

export class Contract_Offer      extends BackboneElement
{

   static def : string = 'Contract_Offer';
   identifier : Identifier [];
   party : Contract_Party [];
   topic : Reference ;
   type : CodeableConcept ;
   decision : CodeableConcept ;
   decisionMode : CodeableConcept [];
   answer : Contract_Answer [];
   text : string ;
   linkId : string [];
   securityLabelNumber : string [];
}
