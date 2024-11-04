import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PartRequest } from 'src/app/models/part-request.class';
import { Observable, map, of } from 'rxjs';
import { PartOffer } from 'src/app/models/part-offer.class';
import { User } from 'src/app/models/user.class';
import { as, revive } from './internal';
import { API_BASE_URL, STRIPE_API_BASE_URL, STRIPE_PRIVATE_KEY } from 'src/app/utils/constants';
import { BeFile } from 'src/app/models/be-file.class';
import { PartOfferComment } from 'src/app/models/part-offer-comment.class';
import { PartMasterData } from 'src/app/models/part-master-data.class';
import { ComponentType } from 'src/app/models/enums';
import { PaymentIntent } from '@stripe/stripe-js';

export interface BaseFilter {
  nullField?: string;
  notNullField?: string;
  deleted?: boolean;
  pageSize?: number;
  offset?: number;
}

export interface PartRequestFilter extends BaseFilter {

}

export interface PartOfferFilter extends BaseFilter {
  requestIds?: number[];
  rootIds?: number[];
}

export interface PartOfferCommentFilter extends BaseFilter {
  offerIds?: number[];
  offerRootIds?: number[];
  enrichWithOffer?: boolean;
}

export interface PartMasterDataFilter extends BaseFilter {
  componentNameSearch?: string;
  componentTypeSearch?: string;
  categorySearch?: string;
  manufacturerSearch?: string;
  partNumberSearch?: string;
  alternatePartNumberSearch?: string;
  componentNames?: string[];
  componentTypes?: ComponentType[];
  categories?: string[];
  manufacturers?: string[];
  partNumbers?: string[];
  alternatePartNumbers?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class BeDataService {

  constructor(
    readonly http: HttpClient,
  ) { }

  countPartRequests$(filter?: PartRequestFilter): Observable<number> {
    return this.http.post<number>(`${API_BASE_URL}/part-request/count`, filter || {});
  }

  @revive(as(PartRequest))
  loadPartRequest$(id: number): Observable<PartRequest> {
    return this.http.get<PartRequest>(`${API_BASE_URL}/part-request/${id}`);
  }

  @revive(as(PartRequest))
  listPartRequests$(filter?: PartRequestFilter): Observable<PartRequest[]> {
    return this.http.post<PartRequest[]>(`${API_BASE_URL}/part-request/list`, filter || {});
  }

  @revive(as(PartRequest))
  savePartRequest$(request: PartRequest): Observable<PartRequest> {
    return this.http.post<PartRequest>(`${API_BASE_URL}/part-request/save`, request);
  }

  @revive(as(PartRequest))
  saveMultiplePartRequests$(requests: PartRequest[]): Observable<PartRequest[]> {
    return this.http.post<PartRequest[]>(`${API_BASE_URL}/part-request/save-multiple`, requests);
  }

  countPartOffers$(filter?: PartOfferFilter): Observable<number> {
    return this.http.post<number>(`${API_BASE_URL}/part-offer/count`, filter || {});
  }

  @revive(as(PartOffer))
  loadPartOffer$(id: number): Observable<PartOffer> {
    return this.http.get<PartOffer>(`${API_BASE_URL}/part-offer/${id}`);
  }

  @revive(as(PartOffer))
  listPartOffers$(filter?: PartOfferFilter): Observable<PartOffer[]> {
    return this.http.post<PartOffer[]>(`${API_BASE_URL}/part-offer/list`, filter || {});
  }

  @revive(as(PartOffer))
  savePartOffer$(offer: PartOffer): Observable<PartOffer> {
    return this.http.post<PartOffer>(`${API_BASE_URL}/part-offer/save`, offer);
  }

  @revive(as(PartOffer))
  saveMultiplePartOffers$(offers: PartOffer[]): Observable<PartOffer[]> {
    return this.http.post<PartOffer[]>(`${API_BASE_URL}/part-offer/save-multiple`, offers);
  }

  countPartOfferComments$(filter?: PartOfferCommentFilter): Observable<number> {
    return this.http.post<number>(`${API_BASE_URL}/comments/count`, filter || {});
  }

  @revive(as(PartOfferComment))
  listPartOfferComments$(filter?: PartOfferCommentFilter): Observable<PartOfferComment[]> {
    return this.http.post<PartOfferComment[]>(`${API_BASE_URL}/comments/list`, filter || {});
  }

  @revive(as(PartOfferComment))
  savePartOfferComment$(offer: PartOfferComment): Observable<PartOfferComment> {
    return this.http.post<PartOfferComment>(`${API_BASE_URL}/comments/save`, offer);
  }

  @revive(as(PartOfferComment))
  saveMultiplePartOfferComments$(offers: PartOfferComment[]): Observable<PartOfferComment[]> {
    return this.http.post<PartOfferComment[]>(`${API_BASE_URL}/comments/save-multiple`, offers);
  }

  @revive(as(PartMasterData))
  listPartMasterData$(filter?: PartMasterDataFilter): Observable<PartMasterData[]> {
    return this.http.post<PartMasterData[]>(`${API_BASE_URL}/master-data/list`, filter || {});
  }

  @revive(as(User))
  currentUser$(): Observable<User> {
    return this.http.get<User>(`${API_BASE_URL}/user/current-user`);
  }

  @revive(as(User))
  register$(user: User): Observable<User> {
    return this.http.post<User>(`${API_BASE_URL}/user/register`, user);
  }

  login$(username: string, password: string): Observable<string> {
    const formData = new URLSearchParams();
    formData.set('j_username', username);
    formData.set('j_password', password);
    return this.http.post('j_security_check', formData.toString(), {
      responseType: 'text',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' } 
    });
  }

  logout$(): Observable<void> {
    return this.http.get<void>(`${API_BASE_URL}/user/logout`);
  }

  @revive(as(BeFile))
  uploadDocument$(file: File): Observable<BeFile> {
    const formData = new FormData();
    formData.append('name', file.name);
    formData.append('size', file.size.toString());
    formData.append('mimetype', file.type);
    formData.append('file', file);
    formData.append('created', '');
    formData.append('creator', '');
    return this.http.post<BeFile>(API_BASE_URL + '/document/save', formData);
  }

  createPaymentIntent$(request: any): Observable<PaymentIntent> {
    const formData = new URLSearchParams();
    Object.keys(request).forEach((key) => formData.set(key, request[key]));
    return this.http.post<PaymentIntent>(STRIPE_API_BASE_URL + '/payment_intents', formData, {
      headers: new HttpHeaders()
        .append('Content-Type', 'application/x-www-form-urlencoded')
        .append('Authorization', `Bearer ${STRIPE_PRIVATE_KEY}`) 
      });
  }

}
