import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Expression } from './Expression'
import { Measure_Component } from './Measure_Component'

export class Measure_Stratifier      extends BackboneElement
{

   static def : string = 'Measure_Stratifier';
   code : CodeableConcept ;
   description : string ;
   criteria : Expression ;
   component : Measure_Component [];
}
