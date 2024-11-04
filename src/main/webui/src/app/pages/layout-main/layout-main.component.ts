import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Observable, map, shareReplay } from 'rxjs';
import { BaseComponent } from 'src/app/components/base/base.component';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-layout-main',
  templateUrl: './layout-main.component.html',
  styleUrls: ['./layout-main.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LayoutMainComponent extends BaseComponent {

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

  constructor(readonly router: Router, private readonly auth: AuthService) {
    super();
  }
  
}
