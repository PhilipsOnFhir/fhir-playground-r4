import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Period } from './Period'
import { Quantity } from './Quantity'
import { Range } from './Range'
import { Reference } from './Reference'

export class Group_Characteristic      extends BackboneElement
{

   static def : string = 'Group_Characteristic';
   code : CodeableConcept ;
   valueCodeableConcept : CodeableConcept ;
   valueBoolean : boolean ;
   valueQuantity : Quantity ;
   valueRange : Range ;
   valueReference : Reference ;
   exclude : boolean ;
   period : Period ;
}
