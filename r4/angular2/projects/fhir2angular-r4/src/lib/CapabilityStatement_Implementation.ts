import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class CapabilityStatement_Implementation      extends BackboneElement
{

   static def : string = 'CapabilityStatement_Implementation';
   description : string ;
   url : string ;
   custodian : Reference ;
}
