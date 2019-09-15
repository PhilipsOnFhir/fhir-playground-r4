import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { MedicinalProductPharmaceutical_WithdrawalPeriod } from './MedicinalProductPharmaceutical_WithdrawalPeriod'

export class MedicinalProductPharmaceutical_TargetSpecies      extends BackboneElement
{

   static def : string = 'MedicinalProductPharmaceutical_TargetSpecies';
   code : CodeableConcept ;
   withdrawalPeriod : MedicinalProductPharmaceutical_WithdrawalPeriod [];
}
