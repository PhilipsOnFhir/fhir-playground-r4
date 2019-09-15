import { CodeableConcept } from './CodeableConcept'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Element } from './Element'
import { Quantity } from './Quantity'
import { Range } from './Range'
import { Reference } from './Reference'

export class UsageContext      extends Element
{

   static def : string = 'UsageContext';
   code : Coding ;
   valueCodeableConcept : CodeableConcept ;
   valueQuantity : Quantity ;
   valueRange : Range ;
   valueReference : Reference ;
}
