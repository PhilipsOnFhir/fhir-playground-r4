import { BackboneElement } from './BackboneElement'
import { DomainResource } from './DomainResource'
import { ProvenanceEntityRoleEnum } from './ProvenanceEntityRoleEnum'
import { Provenance_Agent } from './Provenance_Agent'
import { Reference } from './Reference'

export class Provenance_Entity      extends BackboneElement
{

   static def : string = 'Provenance_Entity';
   role : ProvenanceEntityRoleEnum ;
   what : Reference ;
   agent : Provenance_Agent [];
}
