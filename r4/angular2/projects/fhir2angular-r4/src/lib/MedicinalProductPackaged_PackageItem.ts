import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { ProdCharacteristic } from './ProdCharacteristic'
import { ProductShelfLife } from './ProductShelfLife'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class MedicinalProductPackaged_PackageItem      extends BackboneElement
{

   static def : string = 'MedicinalProductPackaged_PackageItem';
   identifier : Identifier [];
   type : CodeableConcept ;
   quantity : Quantity ;
   material : CodeableConcept [];
   alternateMaterial : CodeableConcept [];
   device : Reference [];
   manufacturedItem : Reference [];
   packageItem : MedicinalProductPackaged_PackageItem [];
   physicalCharacteristics : ProdCharacteristic ;
   otherCharacteristics : CodeableConcept [];
   shelfLifeStorage : ProductShelfLife [];
   manufacturer : Reference [];
}
