import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { ElementDefinition_Discriminator } from './ElementDefinition_Discriminator'
import { SlicingRulesEnum } from './SlicingRulesEnum'

export class ElementDefinition_Slicing      extends BackboneElement
{

   static def : string = 'ElementDefinition_Slicing';
   discriminator : ElementDefinition_Discriminator [];
   description : string ;
   ordered : boolean ;
   rules : SlicingRulesEnum ;
}
