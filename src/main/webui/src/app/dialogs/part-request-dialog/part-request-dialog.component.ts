import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Inject } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { GridApi } from 'ag-grid-community';
import { merge } from 'lodash-es';
import { DynamicDialogConfig } from 'primeng/dynamicdialog';
import { combineLatest, take, takeUntil } from 'rxjs';
import { BaseComponent } from 'src/app/components/base/base.component';
import { PartMasterData } from 'src/app/models/part-master-data.class';
import { PartRequestState } from 'src/app/models/part-request.class';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';

@Component({
  selector: 'app-part-request-dialog',
  templateUrl: './part-request-dialog.component.html',
  styleUrls: ['./part-request-dialog.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PartRequestDialogComponent extends BaseComponent {

  constructor(
    @Inject(DIALOG_DATA)
    readonly dialogData: { gridApi: GridApi },
    readonly appState: AppStateService,
    private readonly dialogRef: DialogRef,
    private readonly beData: BeDataService,
  ) {
    super();
  }

  autofill(value: any, form: FormGroup): void {
    Object.keys(value).forEach((key) => {
      if ((form.value.autofillEnabled && !form.value[key]) || (form.value[key] instanceof PartMasterData)) {
        form.patchValue({ [key]: value[key] });
      }
    });
  }

  cancel(): void {
    this.dialogRef.close(false);
  }

  save(): void {
    this.appState.partRequest$.pipe(take(1)).subscribe((partRequest) => {
      const dataToSave = partRequest.getDataToSave();
      dataToSave.state = PartRequestState.IN_PROGRESS;
      this.beData.savePartRequest$(dataToSave).pipe(takeUntil(this.destroyed$)).subscribe((savedPartRequest) => {
        this.appState.reloadRequests$.next();
        this.dialogRef.close();
        this.dialogData.gridApi.applyTransaction({ update: [`REQUEST - ${savedPartRequest.id}`]});
      });
    })
  }

}
