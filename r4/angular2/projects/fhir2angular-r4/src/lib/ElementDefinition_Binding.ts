import { BackboneElement } from './BackboneElement'
import { BindingStrengthEnum } from './BindingStrengthEnum'
import { DomainResource } from './DomainResource'

export class ElementDefinition_Binding      extends BackboneElement
{

   static def : string = 'ElementDefinition_Binding';
   strength : BindingStrengthEnum ;
   description : string ;
   valueSet : string ;
}
