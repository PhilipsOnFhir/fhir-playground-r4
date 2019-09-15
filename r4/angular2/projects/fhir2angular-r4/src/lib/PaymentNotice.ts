import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { FinancialResourceStatusCodesEnum } from './FinancialResourceStatusCodesEnum'
import { Identifier } from './Identifier'
import { Money } from './Money'
import { Reference } from './Reference'

export class PaymentNotice      extends DomainResource
{

   static def : string = 'PaymentNotice';
   identifier : Identifier [];
   status : FinancialResourceStatusCodesEnum ;
   request : Reference ;
   response : Reference ;
   created : string ;
   provider : Reference ;
   payment : Reference ;
   paymentDate : string ;
   payee : Reference ;
   recipient : Reference ;
   amount : Money ;
   paymentStatus : CodeableConcept ;
}
