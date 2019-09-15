import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { ContactPoint } from './ContactPoint'
import { DeviceDefinition_Capability } from './DeviceDefinition_Capability'
import { DeviceDefinition_DeviceName } from './DeviceDefinition_DeviceName'
import { DeviceDefinition_Material } from './DeviceDefinition_Material'
import { DeviceDefinition_Property } from './DeviceDefinition_Property'
import { DeviceDefinition_Specialization } from './DeviceDefinition_Specialization'
import { DeviceDefinition_UdiDeviceIdentifier } from './DeviceDefinition_UdiDeviceIdentifier'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { ProdCharacteristic } from './ProdCharacteristic'
import { ProductShelfLife } from './ProductShelfLife'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class DeviceDefinition      extends DomainResource
{

   static def : string = 'DeviceDefinition';
   identifier : Identifier [];
   udiDeviceIdentifier : DeviceDefinition_UdiDeviceIdentifier [];
   manufacturerString : string ;
   manufacturerReference : Reference ;
   deviceName : DeviceDefinition_DeviceName [];
   modelNumber : string ;
   type : CodeableConcept ;
   specialization : DeviceDefinition_Specialization [];
   version : string [];
   safety : CodeableConcept [];
   shelfLifeStorage : ProductShelfLife [];
   physicalCharacteristics : ProdCharacteristic ;
   languageCode : CodeableConcept [];
   capability : DeviceDefinition_Capability [];
   property : DeviceDefinition_Property [];
   owner : Reference ;
   contact : ContactPoint [];
   url : string ;
   onlineInformation : string ;
   note : Annotation [];
   quantity : Quantity ;
   parentDevice : Reference ;
   material : DeviceDefinition_Material [];
}
