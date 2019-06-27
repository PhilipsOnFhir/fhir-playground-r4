import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { InsurancePlan_Benefit1 } from './InsurancePlan_Benefit1'

export class InsurancePlan_SpecificCost      extends BackboneElement
{

   static def : string = 'InsurancePlan_SpecificCost';
   category : CodeableConcept ;
   benefit : InsurancePlan_Benefit1 [];
}
