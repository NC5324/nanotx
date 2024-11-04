import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CdkDialogContainer } from '@angular/cdk/dialog';

@Component({
  selector: 'app-side-dialog',
  templateUrl: './side-dialog.component.html',
  styleUrls: ['./side-dialog.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SideDialogComponent extends CdkDialogContainer {

}
