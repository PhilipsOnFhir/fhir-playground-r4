import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { InsurancePlan_Benefit } from './InsurancePlan_Benefit'
import { Reference } from './Reference'

export class InsurancePlan_Coverage      extends BackboneElement
{

   static def : string = 'InsurancePlan_Coverage';
   type : CodeableConcept ;
   network : Reference [];
   benefit : InsurancePlan_Benefit [];
}
