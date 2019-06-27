import { BackboneElement } from './BackboneElement'
import { Coding } from './Coding'
import { DomainResource } from './DomainResource'

export class Contract_SecurityLabel      extends BackboneElement
{

   static def : string = 'Contract_SecurityLabel';
   number : string [];
   classification : Coding ;
   category : Coding [];
   control : Coding [];
}
