import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class Device_Specialization      extends BackboneElement
{

   static def : string = 'Device_Specialization';
   systemType : CodeableConcept ;
   version : string ;
}
