import { Attachment } from './Attachment'
import { ClaimProcessingCodesEnum } from './ClaimProcessingCodesEnum'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { ExplanationOfBenefitStatusEnum } from './ExplanationOfBenefitStatusEnum'
import { ExplanationOfBenefit_Accident } from './ExplanationOfBenefit_Accident'
import { ExplanationOfBenefit_AddItem } from './ExplanationOfBenefit_AddItem'
import { ExplanationOfBenefit_Adjudication } from './ExplanationOfBenefit_Adjudication'
import { ExplanationOfBenefit_BenefitBalance } from './ExplanationOfBenefit_BenefitBalance'
import { ExplanationOfBenefit_CareTeam } from './ExplanationOfBenefit_CareTeam'
import { ExplanationOfBenefit_Diagnosis } from './ExplanationOfBenefit_Diagnosis'
import { ExplanationOfBenefit_Insurance } from './ExplanationOfBenefit_Insurance'
import { ExplanationOfBenefit_Item } from './ExplanationOfBenefit_Item'
import { ExplanationOfBenefit_Payee } from './ExplanationOfBenefit_Payee'
import { ExplanationOfBenefit_Payment } from './ExplanationOfBenefit_Payment'
import { ExplanationOfBenefit_Procedure } from './ExplanationOfBenefit_Procedure'
import { ExplanationOfBenefit_ProcessNote } from './ExplanationOfBenefit_ProcessNote'
import { ExplanationOfBenefit_Related } from './ExplanationOfBenefit_Related'
import { ExplanationOfBenefit_SupportingInfo } from './ExplanationOfBenefit_SupportingInfo'
import { ExplanationOfBenefit_Total } from './ExplanationOfBenefit_Total'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { Reference } from './Reference'
import { UseEnum } from './UseEnum'

export class ExplanationOfBenefit      extends DomainResource
{

   static def : string = 'ExplanationOfBenefit';
   identifier : Identifier [];
   status : ExplanationOfBenefitStatusEnum ;
   type : CodeableConcept ;
   subType : CodeableConcept ;
   use : UseEnum ;
   patient : Reference ;
   billablePeriod : Period ;
   created : string ;
   enterer : Reference ;
   insurer : Reference ;
   provider : Reference ;
   priority : CodeableConcept ;
   fundsReserveRequested : CodeableConcept ;
   fundsReserve : CodeableConcept ;
   related : ExplanationOfBenefit_Related [];
   prescription : Reference ;
   originalPrescription : Reference ;
   payee : ExplanationOfBenefit_Payee ;
   referral : Reference ;
   facility : Reference ;
   claim : Reference ;
   claimResponse : Reference ;
   outcome : ClaimProcessingCodesEnum ;
   disposition : string ;
   preAuthRef : string [];
   preAuthRefPeriod : Period [];
   careTeam : ExplanationOfBenefit_CareTeam [];
   supportingInfo : ExplanationOfBenefit_SupportingInfo [];
   diagnosis : ExplanationOfBenefit_Diagnosis [];
   procedure : ExplanationOfBenefit_Procedure [];
   precedence : string ;
   insurance : ExplanationOfBenefit_Insurance [];
   accident : ExplanationOfBenefit_Accident ;
   item : ExplanationOfBenefit_Item [];
   addItem : ExplanationOfBenefit_AddItem [];
   adjudication : ExplanationOfBenefit_Adjudication [];
   total : ExplanationOfBenefit_Total [];
   payment : ExplanationOfBenefit_Payment ;
   formCode : CodeableConcept ;
   form : Attachment ;
   processNote : ExplanationOfBenefit_ProcessNote [];
   benefitPeriod : Period ;
   benefitBalance : ExplanationOfBenefit_BenefitBalance [];
}
