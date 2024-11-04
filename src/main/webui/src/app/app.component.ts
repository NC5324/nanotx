import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AuthService } from './services/auth/auth.service';
import { Observable, map, shareReplay, tap } from 'rxjs';
import { BaseComponent } from './components/base/base.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent extends BaseComponent {

  readonly menuItems$: Observable<MenuItem[]> = this.auth.currentUser$.pipe(
    map((currentUser) => [
      {
        label: '',
        align: 'start',
      },
      {
        label: currentUser ? `${currentUser.firstName} ${currentUser.lastName}` : 'Profile',
        icon: 'pi pi-fw pi-user',
        align: 'end',
        items: [
          {
            label: 'About',
            icon: 'pi pi-info-circle',
            routerLink: '/about',
          },
          {
            label: 'Profile',
            icon: 'pi pi-user',
            routerLink: '/profile',
            visible: !!currentUser
          },
          {
            label: 'Sign in',
            icon: 'pi pi-sign-in',
            routerLink: '/login',
            visible: !currentUser
          },
          {
            label: 'Sign up',
            icon: 'pi pi-key',
            routerLink: '/register',
            visible: !currentUser
          },
          {
            label: 'Sign out',
            icon: 'pi pi-sign-out',
            command: () => this.auth.signOut(),
            visible: !!currentUser
          }
        ]
      }
    ]),
    shareReplay(1),
  );

  readonly sideMenuItems$: Observable<MenuItem[]> = this.auth.currentUser$.pipe(
    map((currentUser) => [
      {
        label: 'Home',
        align: 'start',
        icon: currentUser ? 'pi pi-list' : 'pi pi-home',
        routerLink: currentUser ? '/' : '/landing',
      },
      {
        label: 'About',
        align: 'start',
        icon: 'pi pi-question-circle',
        routerLink: '/about',
      }
    ])
  );

  constructor(readonly router: Router, private readonly auth: AuthService) {
    super();
  }

}
