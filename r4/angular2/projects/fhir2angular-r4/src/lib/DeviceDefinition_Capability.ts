import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class DeviceDefinition_Capability      extends BackboneElement
{

   static def : string = 'DeviceDefinition_Capability';
   type : CodeableConcept ;
   description : CodeableConcept [];
}
