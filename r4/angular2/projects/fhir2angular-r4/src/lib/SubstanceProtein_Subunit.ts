import { Attachment } from './Attachment'
import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'

export class SubstanceProtein_Subunit      extends BackboneElement
{

   static def : string = 'SubstanceProtein_Subunit';
   subunit : string ;
   sequence : string ;
   length : string ;
   sequenceAttachment : Attachment ;
   nTerminalModificationId : Identifier ;
   nTerminalModification : string ;
   cTerminalModificationId : Identifier ;
   cTerminalModification : string ;
}
