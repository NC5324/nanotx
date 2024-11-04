import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject, take, takeUntil } from 'rxjs';
import { BaseComponent } from 'src/app/components/base/base.component';
import { AuthService } from 'src/app/services/auth/auth.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';
import { reloadBase } from 'src/app/utils/utils';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SigninComponent extends BaseComponent implements OnInit {

  readonly loginForm = new FormGroup({
    username: new FormControl<any>(null, Validators.required),
    password: new FormControl<any>(null, Validators.required)
  });

  constructor(
    readonly router: Router,
    private readonly auth: AuthService,
    private readonly beData: BeDataService,
  ) {
    super();
  }

  ngOnInit(): void { }

  authenticate(): void {
    this.auth.signIn(this.loginForm.value.username, this.loginForm.value.password);
  }

}
