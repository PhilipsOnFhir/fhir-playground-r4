import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { ImplementationGuide_Grouping } from './ImplementationGuide_Grouping'
import { ImplementationGuide_Page } from './ImplementationGuide_Page'
import { ImplementationGuide_Parameter } from './ImplementationGuide_Parameter'
import { ImplementationGuide_Resource } from './ImplementationGuide_Resource'
import { ImplementationGuide_Template } from './ImplementationGuide_Template'

export class ImplementationGuide_Definition      extends BackboneElement
{

   static def : string = 'ImplementationGuide_Definition';
   grouping : ImplementationGuide_Grouping [];
   resource : ImplementationGuide_Resource [];
   page : ImplementationGuide_Page ;
   parameter : ImplementationGuide_Parameter [];
   template : ImplementationGuide_Template [];
}
