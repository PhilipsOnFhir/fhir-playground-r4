import { BackboneElement } from './BackboneElement'
import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'

export class RelatedPerson_Communication      extends BackboneElement
{

   static def : string = 'RelatedPerson_Communication';
   language : CodeableConcept ;
   preferred : boolean ;
}
