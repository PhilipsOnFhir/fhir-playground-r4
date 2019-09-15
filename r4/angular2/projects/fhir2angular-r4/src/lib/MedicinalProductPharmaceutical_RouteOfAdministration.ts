import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { MedicinalProductPharmaceutical_TargetSpecies } from './MedicinalProductPharmaceutical_TargetSpecies'
import { Quantity } from './Quantity'
import { Ratio } from './Ratio'

export class MedicinalProductPharmaceutical_RouteOfAdministration      extends BackboneElement
{

   static def : string = 'MedicinalProductPharmaceutical_RouteOfAdministration';
   code : CodeableConcept ;
   firstDose : Quantity ;
   maxSingleDose : Quantity ;
   maxDosePerDay : Quantity ;
   maxDosePerTreatmentPeriod : Ratio ;
   maxTreatmentPeriod : string ;
   targetSpecies : MedicinalProductPharmaceutical_TargetSpecies [];
}
