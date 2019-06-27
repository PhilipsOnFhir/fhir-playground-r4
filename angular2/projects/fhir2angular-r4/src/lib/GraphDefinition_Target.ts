import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { GraphDefinition_Compartment } from './GraphDefinition_Compartment'
import { GraphDefinition_Link } from './GraphDefinition_Link'

export class GraphDefinition_Target      extends BackboneElement
{

   static def : string = 'GraphDefinition_Target';
   type : string ;
   params : string ;
   profile : string ;
   compartment : GraphDefinition_Compartment [];
   link : GraphDefinition_Link [];
}
