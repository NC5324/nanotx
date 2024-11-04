import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { combineLatest, map, share, shareReplay, take, takeUntil } from 'rxjs';
import { BaseComponent } from 'src/app/components/base/base.component';
import { User, UserRole } from 'src/app/models/user.class';
import { AuthService } from 'src/app/services/auth/auth.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SignupComponent extends BaseComponent {

  readonly user = new User();
  readonly form = this.user.getRegisterForm();

  readonly profileTypeOptions = [
    { id: UserRole.BUYER, name: 'Buyer' },
    { id: UserRole.SUPPLIER, name: 'Supplier' }
  ];

  constructor(
    readonly router: Router,
    private readonly auth: AuthService,
    private readonly beData: BeDataService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.assertEqualValues('confirmEmail', 'email');
    this.assertEqualValues('confirmPassword', 'password');
  }

  assertEqualValues(validatedControlName: string, checkedControlName: string) {
    const validated = this.form.get(validatedControlName);
    const checked = this.form.get(checkedControlName);
    if (!validated || !checked) {
      throw new Error('Invalid controls');
    }
    combineLatest([validated.valueChanges, checked.valueChanges]).pipe(takeUntil(this.destroyed$)).subscribe(([validatedValue, checkedValue]) => {
      if (validatedValue !== checkedValue) {
        validated.setErrors({ valuesNotEqual: true });
        return;
      }
      validated.setErrors(null);
    });
  }

  signup(): void {
    this.beData.register$(this.user.getDataToSave()).pipe(takeUntil(this.destroyed$)).subscribe((user) => {
      this.auth.signIn(user.login, this.form.value.password);
    });
  }

}
