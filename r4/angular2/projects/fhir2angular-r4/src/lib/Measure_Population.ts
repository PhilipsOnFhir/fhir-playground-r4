import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'

export class Measure_Population      extends BackboneElement
{

   static def : string = 'Measure_Population';
   code : CodeableConcept ;
   description : string ;
   criteria : Expression ;
}
