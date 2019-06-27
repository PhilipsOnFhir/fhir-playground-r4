import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class ObservationDefinition_QuantitativeDetails      extends BackboneElement
{

   static def : string = 'ObservationDefinition_QuantitativeDetails';
   customaryUnit : CodeableConcept ;
   unit : CodeableConcept ;
   conversionFactor : string ;
   decimalPrecision : string ;
}
