import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { InsurancePlan_Cost } from './InsurancePlan_Cost'

export class InsurancePlan_Benefit1      extends BackboneElement
{

   static def : string = 'InsurancePlan_Benefit1';
   type : CodeableConcept ;
   cost : InsurancePlan_Cost [];
}
