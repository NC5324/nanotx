import { ChangeDetectionStrategy, Component } from '@angular/core';
import { BaseComponent } from 'src/app/components/base/base.component';
import { SideDialogService } from 'src/app/services/side-dialog/side-dialog.service';
import { SigninComponent } from '../signin/signin.component';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HomeComponent extends BaseComponent {

  constructor(readonly auth: AuthService) {
    super(); 
  }

}
