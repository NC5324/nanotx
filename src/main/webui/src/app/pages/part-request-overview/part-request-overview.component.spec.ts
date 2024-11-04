import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartRequestOverviewComponent } from './part-request-overview.component';

describe('PartRequestOverviewComponent', () => {
  let component: PartRequestOverviewComponent;
  let fixture: ComponentFixture<PartRequestOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartRequestOverviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartRequestOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
