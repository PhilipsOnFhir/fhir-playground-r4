import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { ExampleScenario_Step } from './ExampleScenario_Step'

export class ExampleScenario_Alternative      extends BackboneElement
{

   static def : string = 'ExampleScenario_Alternative';
   title : string ;
   description : string ;
   step : ExampleScenario_Step [];
}
