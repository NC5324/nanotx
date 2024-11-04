import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { flatMap, keyBy, sortBy, uniqBy } from 'lodash-es';
import {
  BehaviorSubject,
  distinctUntilChanged,
  map,
  shareReplay,
  switchMap,
  combineLatest,
  noop,
  Observable,
  from,
  Subject,
  tap,
  filter,
  of,
} from 'rxjs';
import { PartOffer, PartOfferState } from 'src/app/models/part-offer.class';
import { PartRequest, PartRequestState } from 'src/app/models/part-request.class';
import { BeDataService } from '../be-data/be-data.service';
import { AuthService } from '../auth/auth.service';
import { MaybeEmpty } from 'src/app/utils/types';
import { ensureTruthy } from 'src/app/utils/rxjs-operators';
import { loadStripe } from '@stripe/stripe-js/pure';
import { Stripe } from '@stripe/stripe-js';
import { STRIPE_PUBLIC_KEY } from 'src/app/utils/constants';
import { PartOfferComment } from 'src/app/models/part-offer-comment.class';
import { PartMasterData } from 'src/app/models/part-master-data.class';
import { ComponentType, Currency } from 'src/app/models/enums';
import { DatePipe } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class AppStateService {

  readonly reloadRequests$ = new BehaviorSubject<void>(noop());
  readonly createPartRequest$ = new BehaviorSubject<boolean>(false);
  readonly partRequestContext$ = new BehaviorSubject<MaybeEmpty<PartRequest>>(undefined);

  readonly partRequests$: Observable<PartRequest[]> = combineLatest([this.auth.currentUser$, this.reloadRequests$]).pipe(
    filter(([user]) => !!user),
    switchMap(() => this.beData.listPartRequests$()),
    shareReplay(1),
  );

  readonly partRequest$: Observable<PartRequest> = this.partRequestContext$.pipe(
    ensureTruthy(),
    distinctUntilChanged(),
    shareReplay(1),
  );

  readonly partRequestForm$ = this.partRequest$.pipe(
    map((request) => request.getForm()),
    shareReplay(1),
  );

  readonly partRequestEditable$ = this.partRequest$.pipe(
    map((partRequest) => partRequest.state === PartRequestState.OPEN),
    shareReplay(1)
  );

  readonly reloadOffers$ = new BehaviorSubject<void>(noop());
  readonly partOfferContext$ = new BehaviorSubject<MaybeEmpty<PartOffer>>(undefined);

  readonly partOffers$ = combineLatest([this.partRequest$, this.reloadOffers$]).pipe(
    switchMap(([partRequest]) => this.beData.listPartOffers$({ requestIds: [partRequest.id] })),
    shareReplay(1)
  );

  readonly partOffer$: Observable<PartOffer> = this.partOfferContext$.pipe(
    ensureTruthy(),
    distinctUntilChanged(),
    shareReplay(1),
  );

  readonly partOfferEditable$ = this.partOffer$.pipe(
    map((partOffer) => partOffer.state === PartOfferState.OPEN),
    shareReplay(1)
  );

  readonly partOfferVersions$: Observable<any[]> = this.partOffer$.pipe(
    switchMap((offer) => offer.rootId ? this.beData.listPartOffers$({ rootIds: [offer.rootId] }).pipe(
      map((versions) => versions.length ? versions.map((version) => ({
        id: version.id,
        label: !version.nextVersionId
          ? `Offer ${version.id} (Current version)`
          : `Offer ${version.id} (${this.datePipe.transform(version.created, 'MMM dd, YYYY @ hh:mm a')})`,
        value: version,
      })) : [{ id: offer.id, label: `${offer.id || 'New offer'} (Current version)`, value: offer }]),
    ) : of([{ id: offer.id, label: `${offer.id || 'New offer'} (Current version)`, value: offer }])),
    map((versions) => versions.sort((a, b) => (b.value.created?.getTime() || 0) - (a.value.created?.getTime() || 0))),
    shareReplay(1),
  );

  readonly reloadPartOfferComments$ = new BehaviorSubject<void>(noop());
  readonly partOfferComments$: Observable<PartOfferComment[]> = combineLatest([this.partOffer$, this.reloadPartOfferComments$]).pipe(
    switchMap(([offer]) => this.beData.listPartOfferComments$({ offerRootIds: [offer.rootId], enrichWithOffer: true })),
    shareReplay(1),
  );

  readonly componentTypeOptions = [{ label: 'Active', value: ComponentType.ACTIVE }, { label: 'Passive', value: ComponentType.PASSIVE }];
  readonly currencyOptions = Object.values(Currency);

  readonly stripe$: Observable<Stripe> = from(loadStripe(STRIPE_PUBLIC_KEY)).pipe(ensureTruthy(), shareReplay(1));

  readonly masterDataSearch$ = {
    componentName: new Subject<string>(),
    componentType: new Subject<string>(),
    category: new Subject<string>(),
    manufacturer: new Subject<string>(),
    partNumber: new Subject<string>(),
    alternatePartNumber: new Subject<string>()
  };

  readonly masterDataSuggestions$: { [key: string]: Observable<any[]> } = Object.entries(this.masterDataSearch$).reduce(
    (res, [key, search$]) => ({
      ...res,
      [key]: search$.pipe(
        switchMap((search) => this.beData.listPartMasterData$({ [`${key}Search`]: search }).pipe(
          map((suggestions) => [PartMasterData.revive({ [key]: search } as unknown as PartMasterData)].concat(suggestions))
        ))
      ),
    }),
    {}
  );

  constructor(private readonly auth: AuthService, private readonly beData: BeDataService, private readonly datePipe: DatePipe) {
    this.onRequestEditableChanged();
    this.onOfferEditableChanged();
  }

  onRequestEditableChanged(): void {
    combineLatest([this.partRequest$, this.partRequestEditable$]).subscribe(([request, editable]) => {
      if (!editable) {
        request.getForm().disable();
      } else {
        request.getForm().enable();
      }
    });
  }

  onOfferEditableChanged(): void {
    combineLatest([this.partOffer$, this.partOfferEditable$]).subscribe(([offer, editable]) => {
      if (!editable) {
        offer.getForm().disable();
      } else {
        offer.getForm().enable();
      }
    });
  }

}
