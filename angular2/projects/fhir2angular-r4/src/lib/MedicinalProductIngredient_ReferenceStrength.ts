import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Ratio } from './Ratio'

export class MedicinalProductIngredient_ReferenceStrength      extends BackboneElement
{

   static def : string = 'MedicinalProductIngredient_ReferenceStrength';
   substance : CodeableConcept ;
   strength : Ratio ;
   strengthLowLimit : Ratio ;
   measurementPoint : string ;
   country : CodeableConcept [];
}
