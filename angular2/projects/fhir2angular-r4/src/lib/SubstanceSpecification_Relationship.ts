import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'
import { Range } from './Range'
import { Ratio } from './Ratio'
import { Reference } from './Reference'

export class SubstanceSpecification_Relationship      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_Relationship';
   substanceReference : Reference ;
   substanceCodeableConcept : CodeableConcept ;
   relationship : CodeableConcept ;
   isDefining : boolean ;
   amountQuantity : Quantity ;
   amountRange : Range ;
   amountRatio : Ratio ;
   amountString : string ;
   amountRatioLowLimit : Ratio ;
   amountType : CodeableConcept ;
   source : Reference [];
}
