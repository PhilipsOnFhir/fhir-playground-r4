import { BackboneElement } from './BackboneElement'
import { CatalogEntryRelationTypeEnum } from './CatalogEntryRelationTypeEnum'
import { DomainResource } from './DomainResource'
import { Reference } from './Reference'

export class CatalogEntry_RelatedEntry      extends BackboneElement
{

   static def : string = 'CatalogEntry_RelatedEntry';
   relationtype : CatalogEntryRelationTypeEnum ;
   item : Reference ;
}
