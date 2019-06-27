import { CodeableConcept } from './CodeableConcept'
import { CoverageEligibilityRequest_Insurance } from './CoverageEligibilityRequest_Insurance'
import { CoverageEligibilityRequest_Item } from './CoverageEligibilityRequest_Item'
import { CoverageEligibilityRequest_SupportingInfo } from './CoverageEligibilityRequest_SupportingInfo'
import { DomainResource } from './DomainResource'
import { EligibilityRequestPurposeEnum } from './EligibilityRequestPurposeEnum'
import { FinancialResourceStatusCodesEnum } from './FinancialResourceStatusCodesEnum'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { Reference } from './Reference'

export class CoverageEligibilityRequest      extends DomainResource
{

   static def : string = 'CoverageEligibilityRequest';
   identifier : Identifier [];
   status : FinancialResourceStatusCodesEnum ;
   priority : CodeableConcept ;
   purpose : EligibilityRequestPurposeEnum [];
   patient : Reference ;
   servicedDate : string ;
   servicedPeriod : Period ;
   created : string ;
   enterer : Reference ;
   provider : Reference ;
   insurer : Reference ;
   facility : Reference ;
   supportingInfo : CoverageEligibilityRequest_SupportingInfo [];
   insurance : CoverageEligibilityRequest_Insurance [];
   item : CoverageEligibilityRequest_Item [];
}
