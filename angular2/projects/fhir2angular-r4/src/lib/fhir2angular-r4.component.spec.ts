import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { Fhir2angularR4Component } from './fhir2angular-r4.component';

describe('Fhir2angularR4Component', () => {
  let component: Fhir2angularR4Component;
  let fixture: ComponentFixture<Fhir2angularR4Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ Fhir2angularR4Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(Fhir2angularR4Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
