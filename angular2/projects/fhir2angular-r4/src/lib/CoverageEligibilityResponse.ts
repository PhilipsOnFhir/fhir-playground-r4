import { CodeableConcept } from './CodeableConcept'
import { CoverageEligibilityResponse_Error } from './CoverageEligibilityResponse_Error'
import { CoverageEligibilityResponse_Insurance } from './CoverageEligibilityResponse_Insurance'
import { DomainResource } from './DomainResource'
import { EligibilityResponsePurposeEnum } from './EligibilityResponsePurposeEnum'
import { FinancialResourceStatusCodesEnum } from './FinancialResourceStatusCodesEnum'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { Reference } from './Reference'
import { RemittanceOutcomeEnum } from './RemittanceOutcomeEnum'

export class CoverageEligibilityResponse      extends DomainResource
{

   static def : string = 'CoverageEligibilityResponse';
   identifier : Identifier [];
   status : FinancialResourceStatusCodesEnum ;
   purpose : EligibilityResponsePurposeEnum [];
   patient : Reference ;
   servicedDate : string ;
   servicedPeriod : Period ;
   created : string ;
   requestor : Reference ;
   request : Reference ;
   outcome : RemittanceOutcomeEnum ;
   disposition : string ;
   insurer : Reference ;
   insurance : CoverageEligibilityResponse_Insurance [];
   preAuthRef : string ;
   form : CodeableConcept ;
   error : CoverageEligibilityResponse_Error [];
}
