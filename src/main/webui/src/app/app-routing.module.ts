import { NgModule, inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { PartRequestOverviewComponent } from './pages/part-request-overview/part-request-overview.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { SigninComponent } from './pages/signin/signin.component';
import { SignupComponent } from './pages/signup/signup.component';
import { AuthService } from './services/auth/auth.service';
import { debounceTime, map, tap } from 'rxjs';
import { LayoutMainComponent } from './pages/layout-main/layout-main.component';
import { HomeComponent } from './pages/home/home.component';
import { AboutComponent } from './pages/about/about.component';

const redirectIfAuthenticated = (redirectTo: any) => () => {
  const router = inject(Router);
  const auth = inject(AuthService);
  return auth.authenticated$.pipe(
    map((authenticated) => {
      if (authenticated) {
        router.navigate([redirectTo]);
        return false;
      }
      return true;
    })
  );
}

const routes: Routes = [
  {
    path: 'landing',
    pathMatch: 'full',
    component: HomeComponent,
  },
  {
    path: 'about',
    pathMatch: 'full',
    component: AboutComponent,
  },
  {
    path: 'login',
    pathMatch: 'full',
    component: SigninComponent,
    canActivate: [redirectIfAuthenticated('/')],
  },
  {
    path: 'register',
    pathMatch: 'full',
    component: SignupComponent,
    canActivate: [redirectIfAuthenticated('/')],
  },
  {
    path: 'offer',
    pathMatch: 'full',
    component: PartRequestOverviewComponent,
  },
  {
    path: 'request',
    pathMatch: 'full',
    component: PartRequestOverviewComponent,
  },
  {
    path: '',
    pathMatch: 'full',
    component: PartRequestOverviewComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    paramsInheritanceStrategy: 'always',
    scrollPositionRestoration: 'enabled',
    useHash: true,
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
