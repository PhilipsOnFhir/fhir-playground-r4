import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { MolecularSequence_Roc } from './MolecularSequence_Roc'
import { Quantity } from './Quantity'

export class MolecularSequence_Quality      extends BackboneElement
{

   static def : string = 'MolecularSequence_Quality';
   type : string ;
   standardSequence : CodeableConcept ;
   start : string ;
   end : string ;
   score : Quantity ;
   method : CodeableConcept ;
   truthTP : string ;
   queryTP : string ;
   truthFN : string ;
   queryFP : string ;
   gtFP : string ;
   precision : string ;
   recall : string ;
   fScore : string ;
   roc : MolecularSequence_Roc ;
}
