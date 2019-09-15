import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { MolecularSequence_Inner } from './MolecularSequence_Inner'
import { MolecularSequence_Outer } from './MolecularSequence_Outer'

export class MolecularSequence_StructureVariant      extends BackboneElement
{

   static def : string = 'MolecularSequence_StructureVariant';
   variantType : CodeableConcept ;
   exact : boolean ;
   length : string ;
   outer : MolecularSequence_Outer ;
   inner : MolecularSequence_Inner ;
}
