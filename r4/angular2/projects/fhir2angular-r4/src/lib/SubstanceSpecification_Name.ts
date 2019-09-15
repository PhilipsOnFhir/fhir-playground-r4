import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'
import { SubstanceSpecification_Official } from './SubstanceSpecification_Official'

export class SubstanceSpecification_Name      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_Name';
   name : string ;
   type : CodeableConcept ;
   status : CodeableConcept ;
   preferred : boolean ;
   language : CodeableConcept [];
   domain : CodeableConcept [];
   jurisdiction : CodeableConcept [];
   synonym : SubstanceSpecification_Name [];
   translation : SubstanceSpecification_Name [];
   official : SubstanceSpecification_Official [];
   source : Reference [];
}
