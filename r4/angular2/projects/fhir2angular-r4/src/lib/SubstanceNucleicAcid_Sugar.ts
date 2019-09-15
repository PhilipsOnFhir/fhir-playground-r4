import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'

export class SubstanceNucleicAcid_Sugar      extends BackboneElement
{

   static def : string = 'SubstanceNucleicAcid_Sugar';
   identifier : Identifier ;
   name : string ;
   residueSite : string ;
}
