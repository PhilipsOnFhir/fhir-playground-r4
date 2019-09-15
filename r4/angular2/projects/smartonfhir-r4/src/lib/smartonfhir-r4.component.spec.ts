import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SmartonfhirR4Component } from './smartonfhir-r4.component';

describe('SmartonfhirR4Component', () => {
  let component: SmartonfhirR4Component;
  let fixture: ComponentFixture<SmartonfhirR4Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SmartonfhirR4Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SmartonfhirR4Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
