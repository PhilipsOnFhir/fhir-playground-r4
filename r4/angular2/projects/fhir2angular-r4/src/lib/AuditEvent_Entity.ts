import { AuditEvent_Detail } from './AuditEvent_Detail'
import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class AuditEvent_Entity      extends BackboneElement
{

   static def : string = 'AuditEvent_Entity';
   what : Reference ;
   type : Coding ;
   role : Coding ;
   lifecycle : Coding ;
   securityLabel : Coding [];
   name : string ;
   description : string ;
   query : string ;
   detail : AuditEvent_Detail [];
}
