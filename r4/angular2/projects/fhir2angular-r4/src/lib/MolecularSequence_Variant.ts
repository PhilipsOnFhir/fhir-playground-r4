import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class MolecularSequence_Variant      extends BackboneElement
{

   static def : string = 'MolecularSequence_Variant';
   start : string ;
   end : string ;
   observedAllele : string ;
   referenceAllele : string ;
   cigar : string ;
   variantPointer : Reference ;
}
