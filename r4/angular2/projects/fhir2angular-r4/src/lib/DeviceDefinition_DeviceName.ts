import { BackboneElement } from './BackboneElement'
import { DeviceNameTypeEnum } from './DeviceNameTypeEnum'
import { DomainResource } from './DomainResource'

export class DeviceDefinition_DeviceName      extends BackboneElement
{

   static def : string = 'DeviceDefinition_DeviceName';
   name : string ;
   type : DeviceNameTypeEnum ;
}
