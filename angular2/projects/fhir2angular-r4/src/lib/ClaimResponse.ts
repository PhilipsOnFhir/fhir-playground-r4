import { Attachment } from './Attachment'
import { ClaimProcessingCodesEnum } from './ClaimProcessingCodesEnum'
import { ClaimResponse_AddItem } from './ClaimResponse_AddItem'
import { ClaimResponse_Adjudication } from './ClaimResponse_Adjudication'
import { ClaimResponse_Error } from './ClaimResponse_Error'
import { ClaimResponse_Insurance } from './ClaimResponse_Insurance'
import { ClaimResponse_Item } from './ClaimResponse_Item'
import { ClaimResponse_Payment } from './ClaimResponse_Payment'
import { ClaimResponse_ProcessNote } from './ClaimResponse_ProcessNote'
import { ClaimResponse_Total } from './ClaimResponse_Total'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { FinancialResourceStatusCodesEnum } from './FinancialResourceStatusCodesEnum'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { Reference } from './Reference'
import { UseEnum } from './UseEnum'

export class ClaimResponse      extends DomainResource
{

   static def : string = 'ClaimResponse';
   identifier : Identifier [];
   status : FinancialResourceStatusCodesEnum ;
   type : CodeableConcept ;
   subType : CodeableConcept ;
   use : UseEnum ;
   patient : Reference ;
   created : string ;
   insurer : Reference ;
   requestor : Reference ;
   request : Reference ;
   outcome : ClaimProcessingCodesEnum ;
   disposition : string ;
   preAuthRef : string ;
   preAuthPeriod : Period ;
   payeeType : CodeableConcept ;
   item : ClaimResponse_Item [];
   addItem : ClaimResponse_AddItem [];
   adjudication : ClaimResponse_Adjudication [];
   total : ClaimResponse_Total [];
   payment : ClaimResponse_Payment ;
   fundsReserve : CodeableConcept ;
   formCode : CodeableConcept ;
   form : Attachment ;
   processNote : ClaimResponse_ProcessNote [];
   communicationRequest : Reference [];
   insurance : ClaimResponse_Insurance [];
   error : ClaimResponse_Error [];
}
