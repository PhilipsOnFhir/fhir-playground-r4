import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'

export class Device_Property      extends BackboneElement
{

   static def : string = 'Device_Property';
   type : CodeableConcept ;
   valueQuantity : Quantity [];
   valueCode : CodeableConcept [];
}
