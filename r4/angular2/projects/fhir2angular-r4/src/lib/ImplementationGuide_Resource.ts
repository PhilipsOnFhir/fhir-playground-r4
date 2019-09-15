import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { FHIRVersionEnum } from './FHIRVersionEnum'
import { Reference } from './Reference'

export class ImplementationGuide_Resource      extends BackboneElement
{

   static def : string = 'ImplementationGuide_Resource';
   reference : Reference ;
   fhirVersion : FHIRVersionEnum [];
   name : string ;
   description : string ;
   exampleBoolean : boolean ;
   exampleCanonical : string ;
   groupingId : string ;
}
