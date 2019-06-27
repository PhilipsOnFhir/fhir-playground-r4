import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'

export class ImagingStudy_Instance      extends BackboneElement
{

   static def : string = 'ImagingStudy_Instance';
   uid : string ;
   sopClass : Coding ;
   number : string ;
   title : string ;
}
