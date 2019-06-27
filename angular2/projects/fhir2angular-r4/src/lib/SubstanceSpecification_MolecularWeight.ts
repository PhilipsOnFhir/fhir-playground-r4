import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'

export class SubstanceSpecification_MolecularWeight      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_MolecularWeight';
   method : CodeableConcept ;
   type : CodeableConcept ;
   amount : Quantity ;
}
