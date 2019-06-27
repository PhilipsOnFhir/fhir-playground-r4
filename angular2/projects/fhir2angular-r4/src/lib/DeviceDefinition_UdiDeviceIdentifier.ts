import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'

export class DeviceDefinition_UdiDeviceIdentifier      extends BackboneElement
{

   static def : string = 'DeviceDefinition_UdiDeviceIdentifier';
   deviceIdentifier : string ;
   issuer : string ;
   jurisdiction : string ;
}
