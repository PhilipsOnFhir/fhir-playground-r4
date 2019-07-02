import { TestBed, inject } from '@angular/core/testing';

import { FhirCastRestService } from './fhir-cast-rest.service';

describe('FhirCastRestService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FhirCastRestService]
    });
  });

  it('should be created', inject([FhirCastRestService], (service: FhirCastRestService) => {
    expect(service).toBeTruthy();
  }));
});
