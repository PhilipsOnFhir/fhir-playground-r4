import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class MeasureReport_Component      extends BackboneElement
{

   static def : string = 'MeasureReport_Component';
   code : CodeableConcept ;
   value : CodeableConcept ;
}
