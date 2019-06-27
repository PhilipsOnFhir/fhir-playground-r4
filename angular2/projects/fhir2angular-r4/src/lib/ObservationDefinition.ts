import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { ObservationDataTypeEnum } from './ObservationDataTypeEnum'
import { ObservationDefinition_QualifiedInterval } from './ObservationDefinition_QualifiedInterval'
import { ObservationDefinition_QuantitativeDetails } from './ObservationDefinition_QuantitativeDetails'
import { Reference } from './Reference'

export class ObservationDefinition      extends DomainResource
{

   static def : string = 'ObservationDefinition';
   category : CodeableConcept [];
   code : CodeableConcept ;
   identifier : Identifier [];
   permittedDataType : ObservationDataTypeEnum [];
   multipleResultsAllowed : boolean ;
   method : CodeableConcept ;
   preferredReportName : string ;
   quantitativeDetails : ObservationDefinition_QuantitativeDetails ;
   qualifiedInterval : ObservationDefinition_QualifiedInterval [];
   validCodedValueSet : Reference ;
   normalCodedValueSet : Reference ;
   abnormalCodedValueSet : Reference ;
   criticalCodedValueSet : Reference ;
}
