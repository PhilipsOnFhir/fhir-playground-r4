import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Sequence_Inner } from './Sequence_Inner'
import { Sequence_Outer } from './Sequence_Outer'

export class Sequence_StructureVariant      extends BackboneElement
{

   static def : string = 'Sequence_StructureVariant';
   variantType : CodeableConcept ;
   exact : boolean ;
   length : string ;
   outer : Sequence_Outer ;
   inner : Sequence_Inner ;
}
