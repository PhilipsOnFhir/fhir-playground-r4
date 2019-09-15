import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { TerminologyCapabilities_Parameter } from './TerminologyCapabilities_Parameter'

export class TerminologyCapabilities_Expansion      extends BackboneElement
{

   static def : string = 'TerminologyCapabilities_Expansion';
   hierarchical : boolean ;
   paging : boolean ;
   incomplete : boolean ;
   parameter : TerminologyCapabilities_Parameter [];
   textFilter : string ;
}
