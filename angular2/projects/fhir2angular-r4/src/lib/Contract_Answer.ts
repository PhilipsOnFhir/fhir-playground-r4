import { Attachment } from './Attachment'
import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class Contract_Answer      extends BackboneElement
{

   static def : string = 'Contract_Answer';
   valueBoolean : boolean ;
   valueDecimal : string ;
   valueInteger : string ;
   valueDate : string ;
   valueDateTime : string ;
   valueTime : string ;
   valueString : string ;
   valueUri : string ;
   valueAttachment : Attachment ;
   valueCoding : Coding ;
   valueQuantity : Quantity ;
   valueReference : Reference ;
}
