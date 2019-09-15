import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { MolecularSequence_Quality } from './MolecularSequence_Quality'
import { MolecularSequence_ReferenceSeq } from './MolecularSequence_ReferenceSeq'
import { MolecularSequence_Repository } from './MolecularSequence_Repository'
import { MolecularSequence_StructureVariant } from './MolecularSequence_StructureVariant'
import { MolecularSequence_Variant } from './MolecularSequence_Variant'
import { Quantity } from './Quantity'
import { Reference } from './Reference'

export class MolecularSequence      extends DomainResource
{

   static def : string = 'MolecularSequence';
   identifier : Identifier [];
   type : string ;
   coordinateSystem : string ;
   patient : Reference ;
   specimen : Reference ;
   device : Reference ;
   performer : Reference ;
   quantity : Quantity ;
   referenceSeq : MolecularSequence_ReferenceSeq ;
   variant : MolecularSequence_Variant [];
   observedSeq : string ;
   quality : MolecularSequence_Quality [];
   readCoverage : string ;
   repository : MolecularSequence_Repository [];
   pointer : Reference [];
   structureVariant : MolecularSequence_StructureVariant [];
}
