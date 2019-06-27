import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'

export class SubstanceNucleicAcid_Linkage      extends BackboneElement
{

   static def : string = 'SubstanceNucleicAcid_Linkage';
   connectivity : string ;
   identifier : Identifier ;
   name : string ;
   residueSite : string ;
}
