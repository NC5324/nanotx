import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartOfferPaymentDialogComponent } from './part-offer-payment-dialog.component';

describe('PartOfferPaymentDialogComponent', () => {
  let component: PartOfferPaymentDialogComponent;
  let fixture: ComponentFixture<PartOfferPaymentDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartOfferPaymentDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PartOfferPaymentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
