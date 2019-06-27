import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { UDIEntryTypeEnum } from './UDIEntryTypeEnum'

export class Device_UdiCarrier      extends BackboneElement
{

   static def : string = 'Device_UdiCarrier';
   deviceIdentifier : string ;
   issuer : string ;
   jurisdiction : string ;
   carrierAIDC : string ;
   carrierHRF : string ;
   entryType : UDIEntryTypeEnum ;
}
