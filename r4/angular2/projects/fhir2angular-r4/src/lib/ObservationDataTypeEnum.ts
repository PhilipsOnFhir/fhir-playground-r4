import { DomainResource } from './DomainResource';

export enum ObservationDataTypeEnum{
    CODEABLECONCEPT = 'CodeableConcept',
    PERIOD = 'Period',
    QUANTITY = 'Quantity',
    RANGE = 'Range',
    RATIO = 'Ratio',
    SAMPLEDDATA = 'SampledData',
    BOOLEAN = 'boolean',
    DATETIME = 'dateTime',
    INTEGER = 'integer',
    STRING = 'string',
    TIME = 'time',
}
