import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { Observable, of, tap } from 'rxjs';
import { PartRequest } from 'src/app/models/part-request.class';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';

@Injectable({
  providedIn: 'root'
})
export class PartRequestResolver implements Resolve<PartRequest> {

  constructor(private readonly beData: BeDataService, private readonly auth: AuthService, private readonly appState: AppStateService) { }

  resolve(route: ActivatedRouteSnapshot): Observable<PartRequest> {
    const params: any = route.params;
    if (!params.id) {
      throw new Error('Invalid ID parameter');
    }
    if (params.id === 'new') {
      const request = new PartRequest();
      this.appState.partRequestContext$.next(request);
      this.appState.partOfferContext$.next(null);
      return of(request);
    }
    return of();
    // return this.beData.getPartRequest$(parseInt(params.id, 10)).pipe(
    //   tap((request) => {
    //     this.appState.partRequestContext$.next(request);
    //     if (request.id !== this.appState.partOfferContext$.value?.partRequest?.id) {
    //       this.appState.partOfferContext$.next(null);
    //     }
    //   })
    // );
  }
}
