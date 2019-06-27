import { BackboneElement } from './BackboneElement'
import { DeviceNameTypeEnum } from './DeviceNameTypeEnum'
import { DomainResource } from './DomainResource'

export class Device_DeviceName      extends BackboneElement
{

   static def : string = 'Device_DeviceName';
   name : string ;
   type : DeviceNameTypeEnum ;
}
