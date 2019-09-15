import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'
import { SubstanceSpecification_Isotope } from './SubstanceSpecification_Isotope'
import { SubstanceSpecification_MolecularWeight } from './SubstanceSpecification_MolecularWeight'
import { SubstanceSpecification_Representation } from './SubstanceSpecification_Representation'

export class SubstanceSpecification_Structure      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_Structure';
   stereochemistry : CodeableConcept ;
   opticalActivity : CodeableConcept ;
   molecularFormula : string ;
   molecularFormulaByMoiety : string ;
   isotope : SubstanceSpecification_Isotope [];
   molecularWeight : SubstanceSpecification_MolecularWeight ;
   source : Reference [];
   representation : SubstanceSpecification_Representation [];
}
