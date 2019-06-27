import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { ObservationStatusEnum } from './ObservationStatusEnum'
import { Observation_Component } from './Observation_Component'
import { Observation_ReferenceRange } from './Observation_ReferenceRange'
import { Period } from './Period'
import { Quantity } from './Quantity'
import { Range } from './Range'
import { Ratio } from './Ratio'
import { Reference } from './Reference'
import { SampledData } from './SampledData'
import { Timing } from './Timing'

export class Observation      extends DomainResource
{

   static def : string = 'Observation';
   identifier : Identifier [];
   basedOn : Reference [];
   partOf : Reference [];
   status : ObservationStatusEnum ;
   category : CodeableConcept [];
   code : CodeableConcept ;
   subject : Reference ;
   focus : Reference [];
   encounter : Reference ;
   effectiveDateTime : string ;
   effectivePeriod : Period ;
   effectiveTiming : Timing ;
   effectiveInstant : string ;
   issued : string ;
   performer : Reference [];
   valueQuantity : Quantity ;
   valueCodeableConcept : CodeableConcept ;
   valueString : string ;
   valueBoolean : boolean ;
   valueInteger : string ;
   valueRange : Range ;
   valueRatio : Ratio ;
   valueSampledData : SampledData ;
   valueTime : string ;
   valueDateTime : string ;
   valuePeriod : Period ;
   dataAbsentReason : CodeableConcept ;
   interpretation : CodeableConcept [];
   note : Annotation [];
   bodySite : CodeableConcept ;
   method : CodeableConcept ;
   specimen : Reference ;
   device : Reference ;
   referenceRange : Observation_ReferenceRange [];
   hasMember : Reference [];
   derivedFrom : Reference [];
   component : Observation_Component [];
}
