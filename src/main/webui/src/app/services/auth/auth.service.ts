import { Injectable } from '@angular/core';
import { BehaviorSubject, shareReplay, map, tap, noop } from 'rxjs';
import { User } from 'src/app/models/user.class';
import { BeDataService } from '../be-data/be-data.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { reloadBase } from '../../utils/utils';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  readonly currentUser$ = new BehaviorSubject<User | null>(null);
  readonly authenticated$ = this.currentUser$.pipe(map((currentUser) => !!currentUser), shareReplay(1));
  readonly cookieName = 'quarkus-credential';

  constructor(private readonly router: Router, private readonly beDataService: BeDataService) {
    this.resetCurrentUser();
  }

  signIn(username: string, password: string): void {
    this.beDataService.login$(username, password).subscribe(() => {
      this.resetCurrentUser();
    });
  }

  signOut(): void {
    this.beDataService.logout$().subscribe(() => {
      this.resetCurrentUser();
    });
  }

  resetCurrentUser(): void {
    this.beDataService.currentUser$().subscribe((currentUser) => {
      this.currentUser$.next(currentUser);
      if (!currentUser) {
        this.router.navigate(['/landing']);
      } else {
        this.router.navigate(['/']);
      }
    });
  }

}
