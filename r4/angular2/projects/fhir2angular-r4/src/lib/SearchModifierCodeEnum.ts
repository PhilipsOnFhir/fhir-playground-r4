import { DomainResource } from './DomainResource';

export enum SearchModifierCodeEnum{
    ABOVE = 'above',
    BELOW = 'below',
    CONTAINS = 'contains',
    EXACT = 'exact',
    IDENTIFIER = 'identifier',
    IN = 'in',
    MISSING = 'missing',
    NOT = 'not',
    NOT_IN = 'not-in',
    OFTYPE = 'ofType',
    TEXT = 'text',
    TYPE = 'type',
}
