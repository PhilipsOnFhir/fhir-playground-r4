import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { Coding } from './Coding'
import { Contract_Answer } from './Contract_Answer'
import { Contract_Context } from './Contract_Context'
import { Contract_ValuedItem } from './Contract_ValuedItem'
import { DomainResource } from './DomainResource'
import { Period } from './Period'
import { Reference } from './Reference'

export class Contract_Asset      extends BackboneElement
{

   static def : string = 'Contract_Asset';
   scope : CodeableConcept ;
   type : CodeableConcept [];
   typeReference : Reference [];
   subtype : CodeableConcept [];
   relationship : Coding ;
   context : Contract_Context [];
   condition : string ;
   periodType : CodeableConcept [];
   period : Period [];
   usePeriod : Period [];
   text : string ;
   linkId : string [];
   answer : Contract_Answer [];
   securityLabelNumber : string [];
   valuedItem : Contract_ValuedItem [];
}
