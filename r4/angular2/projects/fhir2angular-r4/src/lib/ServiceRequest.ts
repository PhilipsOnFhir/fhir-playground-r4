import { Annotation } from './Annotation'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { Period } from './Period'
import { Quantity } from './Quantity'
import { Range } from './Range'
import { Ratio } from './Ratio'
import { Reference } from './Reference'
import { RequestIntentEnum } from './RequestIntentEnum'
import { RequestPriorityEnum } from './RequestPriorityEnum'
import { RequestStatusEnum } from './RequestStatusEnum'
import { Timing } from './Timing'

export class ServiceRequest      extends DomainResource
{

   static def : string = 'ServiceRequest';
   identifier : Identifier [];
   instantiatesCanonical : string [];
   instantiatesUri : string [];
   basedOn : Reference [];
   replaces : Reference [];
   requisition : Identifier ;
   status : RequestStatusEnum ;
   intent : RequestIntentEnum ;
   category : CodeableConcept [];
   priority : RequestPriorityEnum ;
   doNotPerform : boolean ;
   code : CodeableConcept ;
   orderDetail : CodeableConcept [];
   quantityQuantity : Quantity ;
   quantityRatio : Ratio ;
   quantityRange : Range ;
   subject : Reference ;
   encounter : Reference ;
   occurrenceDateTime : string ;
   occurrencePeriod : Period ;
   occurrenceTiming : Timing ;
   asNeededBoolean : boolean ;
   asNeededCodeableConcept : CodeableConcept ;
   authoredOn : string ;
   requester : Reference ;
   performerType : CodeableConcept ;
   performer : Reference [];
   locationCode : CodeableConcept [];
   locationReference : Reference [];
   reasonCode : CodeableConcept [];
   reasonReference : Reference [];
   insurance : Reference [];
   supportingInfo : Reference [];
   specimen : Reference [];
   bodySite : CodeableConcept [];
   note : Annotation [];
   patientInstruction : string ;
   relevantHistory : Reference [];
}
