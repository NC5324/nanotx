import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProfileComponent {

}
