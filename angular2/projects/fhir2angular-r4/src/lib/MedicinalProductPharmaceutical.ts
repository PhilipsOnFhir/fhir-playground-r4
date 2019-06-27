import { CodeableConcept } from './CodeableConcept'
import { DomainResource } from './DomainResource'
import { Identifier } from './Identifier'
import { MedicinalProductPharmaceutical_Characteristics } from './MedicinalProductPharmaceutical_Characteristics'
import { MedicinalProductPharmaceutical_RouteOfAdministration } from './MedicinalProductPharmaceutical_RouteOfAdministration'
import { Reference } from './Reference'

export class MedicinalProductPharmaceutical      extends DomainResource
{

   static def : string = 'MedicinalProductPharmaceutical';
   identifier : Identifier [];
   administrableDoseForm : CodeableConcept ;
   unitOfPresentation : CodeableConcept ;
   ingredient : Reference [];
   device : Reference [];
   characteristics : MedicinalProductPharmaceutical_Characteristics [];
   routeOfAdministration : MedicinalProductPharmaceutical_RouteOfAdministration [];
}
