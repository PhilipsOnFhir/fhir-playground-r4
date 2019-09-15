import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { ContactPoint } from './ContactPoint'
import { Device_DeviceName } from './Device_DeviceName'
import { Device_Property } from './Device_Property'
import { Device_Specialization } from './Device_Specialization'
import { Device_UdiCarrier } from './Device_UdiCarrier'
import { Device_Version } from './Device_Version'
import { DomainResource } from './DomainResource'
import { FHIRDeviceStatusEnum } from './FHIRDeviceStatusEnum'
import { Identifier } from './Identifier'
import { Reference } from './Reference'

export class Device      extends DomainResource
{

   static def : string = 'Device';
   identifier : Identifier [];
   definition : Reference ;
   udiCarrier : Device_UdiCarrier [];
   status : FHIRDeviceStatusEnum ;
   statusReason : CodeableConcept [];
   distinctIdentifier : string ;
   manufacturer : string ;
   manufactureDate : string ;
   expirationDate : string ;
   lotNumber : string ;
   serialNumber : string ;
   deviceName : Device_DeviceName [];
   modelNumber : string ;
   partNumber : string ;
   type : CodeableConcept ;
   specialization : Device_Specialization [];
   version : Device_Version [];
   property : Device_Property [];
   patient : Reference ;
   owner : Reference ;
   contact : ContactPoint [];
   location : Reference ;
   url : string ;
   note : Annotation [];
   safety : CodeableConcept [];
   parent : Reference ;
}
