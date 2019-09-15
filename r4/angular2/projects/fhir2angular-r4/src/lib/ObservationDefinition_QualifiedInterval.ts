import { AdministrativeGenderEnum } from './AdministrativeGenderEnum'
import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { ObservationRangeCategoryEnum } from './ObservationRangeCategoryEnum'
import { Range } from './Range'

export class ObservationDefinition_QualifiedInterval      extends BackboneElement
{

   static def : string = 'ObservationDefinition_QualifiedInterval';
   category : ObservationRangeCategoryEnum ;
   range : Range ;
   context : CodeableConcept ;
   appliesTo : CodeableConcept [];
   gender : AdministrativeGenderEnum ;
   age : Range ;
   gestationalAge : Range ;
   condition : string ;
}
