import { BackboneElement } from './BackboneElement'
import { DiscriminatorTypeEnum } from './DiscriminatorTypeEnum'
import { DomainResource } from './DomainResource'

export class ElementDefinition_Discriminator      extends BackboneElement
{

   static def : string = 'ElementDefinition_Discriminator';
   type : DiscriminatorTypeEnum ;
   path : string ;
}
