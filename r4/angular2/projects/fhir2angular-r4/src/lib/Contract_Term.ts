import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { Contract_Action } from './Contract_Action'
import { Contract_Asset } from './Contract_Asset'
import { Contract_Offer } from './Contract_Offer'
import { Contract_SecurityLabel } from './Contract_SecurityLabel'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { Reference } from './Reference'

export class Contract_Term      extends BackboneElement
{

   static def : string = 'Contract_Term';
   identifier : Identifier ;
   issued : string ;
   applies : Period ;
   topicCodeableConcept : CodeableConcept ;
   topicReference : Reference ;
   type : CodeableConcept ;
   subType : CodeableConcept ;
   text : string ;
   securityLabel : Contract_SecurityLabel [];
   offer : Contract_Offer ;
   asset : Contract_Asset [];
   action : Contract_Action [];
   group : Contract_Term [];
}
