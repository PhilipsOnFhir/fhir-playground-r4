import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'

export class MedicinalProductPharmaceutical_WithdrawalPeriod      extends BackboneElement
{

   static def : string = 'MedicinalProductPharmaceutical_WithdrawalPeriod';
   tissue : CodeableConcept ;
   value : Quantity ;
   supportingInformation : string ;
}
