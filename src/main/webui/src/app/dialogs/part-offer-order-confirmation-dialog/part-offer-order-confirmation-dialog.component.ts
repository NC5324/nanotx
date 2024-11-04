import { ChangeDetectionStrategy, Component, ViewChild } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { GridApi, GridReadyEvent } from 'ag-grid-community';
import { intersectionBy, keyBy } from 'lodash-es';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { FileUpload } from 'primeng/fileupload';
import { forkJoin, takeUntil } from 'rxjs';
import { BaseComponent } from 'src/app/components/base/base.component';
import { BeFile } from 'src/app/models/be-file.class';
import { PartOffer } from 'src/app/models/part-offer.class';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';
import { FormValidators } from 'src/app/utils/form-validators.class';

@Component({
  selector: 'app-part-offer-order-confirmation-dialog',
  templateUrl: './part-offer-order-confirmation-dialog.component.html',
  styleUrls: ['./part-offer-order-confirmation-dialog.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PartOfferOrderConfirmationDialogComponent extends BaseComponent {

  @ViewChild(FileUpload) documentUploader!: FileUpload;

  readonly form = new FormGroup({
    documents: new FormControl<BeFile[]>([], FormValidators.requiredLength(1)),
    confirmation: new FormControl<boolean>(false, Validators.requiredTrue),
  });

  private documentsGridApi!: GridApi;

  constructor(
    readonly appState: AppStateService,
    private readonly beData: BeDataService,
    private readonly dialogRef: DynamicDialogRef
  ) {
    super();
  }

  onDocumentsGridReady(event: GridReadyEvent): void {
    this.documentsGridApi = event.api;
  }

  uploadDocuments(fileSelectEvent: any): void {
    if (fileSelectEvent.files) {
      forkJoin(this.getFiles(fileSelectEvent).map((file) => this.beData.uploadDocument$(file))).pipe(takeUntil(this.destroyed$)).subscribe((uploadedFiles) => {
        const documents = this.form.value.documents || [];
        const offer = this.appState.partOfferContext$.value as PartOffer;
        documents.push(...uploadedFiles);
        offer.documents.push(...uploadedFiles);
        this.form.controls.documents.patchValue(documents);
        this.beData.savePartOffer$(offer.getDataToSave()).pipe(takeUntil(this.destroyed$)).subscribe((savedOffer) => {
          this.appState.partOfferContext$.next(savedOffer);
          this.documentsGridApi?.applyTransaction({ add: intersectionBy(documents, uploadedFiles, 'id') });
          this.documentUploader.clear();
        });
      });
    }
  }

  getFiles(fileSelectEvent: any): File[] {
    const files: File[] = [];
    if (fileSelectEvent.files instanceof FileList) {
      for (let index = 0; index < fileSelectEvent.files.length; index++) {
        files.push(fileSelectEvent.files[index]);
      }
    } else {
      files.push(fileSelectEvent.files[0]);
    }
    return files;
  }

  downloadAllDocuments(): void {
    this.form.value.documents?.forEach((document, index) => {
      setTimeout(() => {
        window.location.href = document.url;
      }, index * 1000);
    });
  }

  removeDocument(file: BeFile): void {
    const documents = this.form.value.documents || [];
    const offer = this.appState.partOfferContext$.value as PartOffer;
    const documentById = keyBy(offer.documents, 'id');
    const formDocumentsById = keyBy(documents, 'id');
    delete documentById[file.id];
    delete formDocumentsById[file.id];
    offer.documents = Object.values(documentById);
    this.form.controls.documents.patchValue(documents);
    this.beData.savePartOffer$(offer.getDataToSave()).pipe(takeUntil(this.destroyed$)).subscribe((savedOffer) => {
      this.appState.partOfferContext$.next(savedOffer);
      this.documentsGridApi?.applyTransaction({ remove: [{ id: file.id }] });
    });
  }


  cancel(): void {
    this.dialogRef.close();
  }

  save(): void {
    const offer = this.appState.partOfferContext$.value as PartOffer;
    this.dialogRef.close(offer);
  }

}
