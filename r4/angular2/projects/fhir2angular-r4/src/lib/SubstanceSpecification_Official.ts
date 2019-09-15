import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class SubstanceSpecification_Official      extends BackboneElement
{

   static def : string = 'SubstanceSpecification_Official';
   authority : CodeableConcept ;
   status : CodeableConcept ;
   date : string ;
}
