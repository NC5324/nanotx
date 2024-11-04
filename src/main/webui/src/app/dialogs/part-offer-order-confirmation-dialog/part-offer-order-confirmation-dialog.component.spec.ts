import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartOfferOrderConfirmationDialogComponent } from './part-offer-order-confirmation-dialog.component';

describe('PartOfferOrderConfirmationDialogComponent', () => {
  let component: PartOfferOrderConfirmationDialogComponent;
  let fixture: ComponentFixture<PartOfferOrderConfirmationDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartOfferOrderConfirmationDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartOfferOrderConfirmationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
