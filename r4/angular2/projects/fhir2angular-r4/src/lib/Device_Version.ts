import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'

export class Device_Version      extends BackboneElement
{

   static def : string = 'Device_Version';
   type : CodeableConcept ;
   component : Identifier ;
   value : string ;
}
