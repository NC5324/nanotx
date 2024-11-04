import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of, tap } from 'rxjs';
import { PartOffer } from 'src/app/models/part-offer.class';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';

@Injectable({
  providedIn: 'root'
})
export class PartOfferResolver implements Resolve<PartOffer> {

  constructor(private readonly beData: BeDataService, private readonly auth: AuthService, private readonly appState: AppStateService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<PartOffer> {
    const params: any = route.params;
    if (!params.id) {
      throw new Error('Invalid ID parameter');
    }
    if (params.id === 'new') {
      const offer = new PartOffer();
      this.appState.partOfferContext$.next(offer);
      return of(offer);
    }
    return of();
    // return this.beData.getPartOffer$(parseInt(params.id, 10)).pipe(
      // tap((offer) => this.appState.partOfferContext$.next(offer))
    // );
  }
}
