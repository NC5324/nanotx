import { DialogRef } from '@angular/cdk/dialog';
import { AfterViewInit, ChangeDetectionStrategy, Component, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PaymentIntent, StripeCardElementOptions, StripeElementsOptions, StripePaymentElement } from '@stripe/stripe-js';
import { StripeCardComponent, StripeCardNumberComponent, StripePaymentElementComponent, StripeService } from 'ngx-stripe';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { BehaviorSubject, Observable, map, shareReplay, switchMap, take, takeUntil } from 'rxjs';
import { BaseComponent } from 'src/app/components/base/base.component';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';
import { STRIPE_PRIVATE_KEY, STRIPE_PUBLIC_KEY } from '../../utils/constants';
import { MaybeEmpty } from 'src/app/utils/types';
import { AuthService } from 'src/app/services/auth/auth.service';
import { PartOffer } from 'src/app/models/part-offer.class';
import { currencyFormatter } from 'src/app/utils/utils';

@Component({
  selector: 'app-part-offer-payment-dialog',
  templateUrl: './part-offer-payment-dialog.component.html',
  styleUrls: ['./part-offer-payment-dialog.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PartOfferPaymentDialogComponent extends BaseComponent implements AfterViewInit {

  @ViewChildren('stripePayment')
  private readonly paymentElementQuery?: QueryList<StripePaymentElement>;
  
  @ViewChild(StripeCardComponent) card!: StripeCardComponent;

  readonly cardOptions: StripeCardElementOptions = {
    classes: {
      base: 'stripe-input'
    },
  };

  readonly elementsOptions: StripeElementsOptions = {
    locale: 'en'
  };

  readonly form: FormGroup = new FormGroup({
    address: new FormControl(null, Validators.required),
    address2: new FormControl(null),
    zipCode: new FormControl(null, Validators.required),
    city: new FormControl(null, Validators.required),
    phoneNumber: new FormControl(null, Validators.required),
    paymentMethod: new FormControl('payOnDelivery', Validators.required),
    amount: new FormControl({ value: null, disabled: true }, Validators.required),
  });

  readonly clientSecret$ = new BehaviorSubject<MaybeEmpty<string>>(undefined);

  constructor(
    private readonly beData: BeDataService,
    private readonly dialogRef: DynamicDialogRef,
    private readonly appState: AppStateService,
    private readonly stripeService: StripeService,
    private readonly auth: AuthService,
  ) {
    super();
  }

  ngOnInit() {
    this.appState.partOffer$.pipe(take(1)).subscribe((offer) => {
      const amount = offer.quantity! * offer.targetPrice!;
      this.form.controls['amount'].patchValue(currencyFormatter(offer.currency).format(amount));
      this.beData.createPaymentIntent$({
        amount: amount * 100,
        currency: offer.currency,
      }).pipe(takeUntil(this.destroyed$)).subscribe((paymentIntent) => {
        this.clientSecret$.next(paymentIntent.client_secret);
      });
    });
  }

  ngAfterViewInit(): void {
    this.paymentElementQuery?.changes.pipe(takeUntil(this.destroyed$)).subscribe((paymentElement) => {
      const paymentElementRef: StripePaymentElementComponent = paymentElement.last;
      const nativeWindow = paymentElementRef.stripeElementsService['stripeService'].window.getNativeWindow();
      console.log(paymentElementRef, nativeWindow.document);
    });
  }

  cancel(): void {
    this.dialogRef.close(false);
  }

  save(): void {
    if (this.form.value.paymentMethod === 'payWithCard') {
      this.auth.currentUser$.pipe(take(1)).subscribe((user) => {
        this.stripeService.confirmCardPayment(this.clientSecret$.value as string, {
          payment_method: {
            card: this.card.element,
            billing_details: {
              name: `${user?.firstName} ${user?.lastName}`,
            },
          },
        }).subscribe((result) => {
          if (result.error) {
            alert('Payment failiure: ' + result.error.message);
          } else {
            if (result.paymentIntent.status === 'succeeded') {
              alert('Payment successful');
              const offer = this.appState.partOfferContext$.value as PartOffer;
              offer.getForm().controls['orderDetails'].patchValue(this.form.value);
              this.dialogRef.close(offer);
            }
          }
        });
      });
    } else {
      const offer = this.appState.partOfferContext$.value as PartOffer;
      offer.getForm().controls['orderDetails'].patchValue(this.form.value);
      this.dialogRef.close(true);
    }
  }

}