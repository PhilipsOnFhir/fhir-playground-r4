import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { InsurancePlan_Contact } from './InsurancePlan_Contact'
import { InsurancePlan_Coverage } from './InsurancePlan_Coverage'
import { InsurancePlan_Plan } from './InsurancePlan_Plan'
import { Period } from './Period'
import { PublicationStatusEnum } from './PublicationStatusEnum'
import { Reference } from './Reference'

export class InsurancePlan      extends DomainResource
{

   static def : string = 'InsurancePlan';
   identifier : Identifier [];
   status : PublicationStatusEnum ;
   type : CodeableConcept [];
   name : string ;
   alias : string [];
   period : Period ;
   ownedBy : Reference ;
   administeredBy : Reference ;
   coverageArea : Reference [];
   contact : InsurancePlan_Contact [];
   endpoint : Reference [];
   network : Reference [];
   coverage : InsurancePlan_Coverage [];
   plan : InsurancePlan_Plan [];
}
