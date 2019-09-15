import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'

export class MolecularSequence_Repository      extends BackboneElement
{

   static def : string = 'MolecularSequence_Repository';
   type : string ;
   url : string ;
   name : string ;
   datasetId : string ;
   variantsetId : string ;
   readsetId : string ;
}
