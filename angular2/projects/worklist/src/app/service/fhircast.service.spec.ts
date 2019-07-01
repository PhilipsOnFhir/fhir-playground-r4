import { TestBed } from '@angular/core/testing';

import { FhirCastService } from './fhir-cast.service';

describe('FhirCastService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FhirCastService = TestBed.get(FhirCastService);
    expect(service).toBeTruthy();
  });
});
