import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class AuditEvent_Source      extends BackboneElement
{

   static def : string = 'AuditEvent_Source';
   site : string ;
   observer : Reference ;
   type : Coding [];
}
