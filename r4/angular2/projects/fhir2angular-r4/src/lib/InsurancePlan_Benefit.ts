import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { InsurancePlan_Limit } from './InsurancePlan_Limit'

export class InsurancePlan_Benefit      extends BackboneElement
{

   static def : string = 'InsurancePlan_Benefit';
   type : CodeableConcept ;
   requirement : string ;
   limit : InsurancePlan_Limit [];
}
