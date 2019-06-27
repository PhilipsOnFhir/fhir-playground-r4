import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { ProdCharacteristic } from './ProdCharacteristic'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class MedicinalProductManufactured      extends DomainResource
{

   static def : string = 'MedicinalProductManufactured';
   manufacturedDoseForm : CodeableConcept ;
   unitOfPresentation : CodeableConcept ;
   quantity : Quantity ;
   manufacturer : Reference [];
   ingredient : Reference [];
   physicalCharacteristics : ProdCharacteristic ;
   otherCharacteristics : CodeableConcept [];
}
