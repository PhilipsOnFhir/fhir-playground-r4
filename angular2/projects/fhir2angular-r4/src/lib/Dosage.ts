import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Dosage_DoseAndRate } from './Dosage_DoseAndRate'
import { Quantity } from './Quantity'
import { Ratio } from './Ratio'
import { Timing } from './Timing'

export class Dosage      extends BackboneElement
{

   static def : string = 'Dosage';
   sequence : string ;
   text : string ;
   additionalInstruction : CodeableConcept [];
   patientInstruction : string ;
   timing : Timing ;
   asNeededBoolean : boolean ;
   asNeededCodeableConcept : CodeableConcept ;
   site : CodeableConcept ;
   route : CodeableConcept ;
   method : CodeableConcept ;
   doseAndRate : Dosage_DoseAndRate [];
   maxDosePerPeriod : Ratio ;
   maxDosePerAdministration : Quantity ;
   maxDosePerLifetime : Quantity ;
}
