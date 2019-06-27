import { Attachment } from './Attachment'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Period } from './Period'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class ExplanationOfBenefit_SupportingInfo      extends BackboneElement
{

   static def : string = 'ExplanationOfBenefit_SupportingInfo';
   sequence : string ;
   category : CodeableConcept ;
   code : CodeableConcept ;
   timingDate : string ;
   timingPeriod : Period ;
   valueBoolean : boolean ;
   valueString : string ;
   valueQuantity : Quantity ;
   valueAttachment : Attachment ;
   valueReference : Reference ;
   reason : Coding ;
}
