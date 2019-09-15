import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'

export class MolecularSequence_Roc      extends BackboneElement
{

   static def : string = 'MolecularSequence_Roc';
   score : string [];
   numTP : string [];
   numFP : string [];
   numFN : string [];
   precision : string [];
   sensitivity : string [];
   fMeasure : string [];
}
