import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { Coverage_Exception } from './Coverage_Exception'
import { DomainResource } from './DomainResource'
import { Money } from './Money'
import { Quantity } from './Quantity'

export class Coverage_CostToBeneficiary      extends BackboneElement
{

   static def : string = 'Coverage_CostToBeneficiary';
   type : CodeableConcept ;
   valueQuantity : Quantity ;
   valueMoney : Money ;
   exception : Coverage_Exception [];
}
