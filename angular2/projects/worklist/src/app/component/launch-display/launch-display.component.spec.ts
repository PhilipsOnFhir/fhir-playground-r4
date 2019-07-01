import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LaunchDisplayComponent } from './launch-display.component';

describe('LaunchDisplayComponent', () => {
  let component: LaunchDisplayComponent;
  let fixture: ComponentFixture<LaunchDisplayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LaunchDisplayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LaunchDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
