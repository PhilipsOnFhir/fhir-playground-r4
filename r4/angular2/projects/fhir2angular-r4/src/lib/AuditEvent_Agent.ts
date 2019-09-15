import { AuditEvent_Network } from './AuditEvent_Network'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class AuditEvent_Agent      extends BackboneElement
{

   static def : string = 'AuditEvent_Agent';
   type : CodeableConcept ;
   role : CodeableConcept [];
   who : Reference ;
   altId : string ;
   name : string ;
   requestor : boolean ;
   location : Reference ;
   policy : string [];
   media : Coding ;
   network : AuditEvent_Network ;
   purposeOfUse : CodeableConcept [];
}
