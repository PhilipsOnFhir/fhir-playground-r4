import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'

export class ElementDefinition_Base      extends BackboneElement
{

   static def : string = 'ElementDefinition_Base';
   path : string ;
   min : string ;
   max : string ;
}
