import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Quantity } from './Quantity'
import { SubstanceSpecification_MolecularWeight } from './SubstanceSpecification_MolecularWeight'

export class SubstanceSpecification_Isotope      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_Isotope';
   identifier : Identifier ;
   name : CodeableConcept ;
   substitution : CodeableConcept ;
   halfLife : Quantity ;
   molecularWeight : SubstanceSpecification_MolecularWeight ;
}
