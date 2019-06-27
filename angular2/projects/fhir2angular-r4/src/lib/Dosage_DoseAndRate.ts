import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Quantity } from './Quantity'
import { Range } from './Range'
import { Ratio } from './Ratio'

export class Dosage_DoseAndRate      extends BackboneElement
{

   static def : string = 'Dosage_DoseAndRate';
   type : CodeableConcept ;
   doseRange : Range ;
   doseQuantity : Quantity ;
   rateRatio : Ratio ;
   rateRange : Range ;
   rateQuantity : Quantity ;
}
