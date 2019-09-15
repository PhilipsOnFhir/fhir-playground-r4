import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'

export class MessageDefinition_Focus      extends BackboneElement
{

   static def : string = 'MessageDefinition_Focus';
   code : string ;
   profile : string ;
   min : string ;
   max : string ;
}
