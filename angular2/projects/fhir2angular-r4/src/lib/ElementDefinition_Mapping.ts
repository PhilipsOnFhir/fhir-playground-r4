import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'

export class ElementDefinition_Mapping      extends BackboneElement
{

   static def : string = 'ElementDefinition_Mapping';
   identity : string ;
   language : string ;
   map : string ;
   comment : string ;
}
