import { DIALOG_DATA, DialogRef } from '@angular/cdk/dialog';
import { ChangeDetectionStrategy, Component, Inject, ViewChild, ChangeDetectorRef } from '@angular/core';
import { combineLatest, forkJoin, map, shareReplay, switchMap, take, takeUntil, Subject } from 'rxjs';
import { BaseComponent } from 'src/app/components/base/base.component';
import { PartOffer, PartOfferState } from 'src/app/models/part-offer.class';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { BeDataService } from 'src/app/services/be-data/be-data.service';
import { currencyFormatter } from '../../utils/utils';
import { BeFile } from 'src/app/models/be-file.class';
import { intersectionBy, keyBy } from 'lodash-es';
import { GridApi, GridReadyEvent } from 'ag-grid-community';
import { FileUpload } from 'primeng/fileupload';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ensureTruthy } from 'src/app/utils/rxjs-operators';
import { PartRequestOverviewComponent } from 'src/app/pages/part-request-overview/part-request-overview.component';
import { DialogService } from 'primeng/dynamicdialog';
import { PartOfferPaymentDialogComponent } from '../part-offer-payment-dialog/part-offer-payment-dialog.component';
import { FormGroup } from '@angular/forms';
import { PartMasterData } from 'src/app/models/part-master-data.class';
import { PartRequest } from 'src/app/models/part-request.class';
import { PartOfferCommentDialogComponent } from '../part-offer-comment-dialog/part-offer-comment-dialog.component';
import { PartOfferOrderConfirmationDialogComponent } from '../part-offer-order-confirmation-dialog/part-offer-order-confirmation-dialog.component';

@Component({
  selector: 'app-part-offer-dialog',
  templateUrl: './part-offer-dialog.component.html',
  styleUrls: ['./part-offer-dialog.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PartOfferDialogComponent extends BaseComponent {

  @ViewChild(FileUpload) documentUploader!: FileUpload;

  readonly asyncData$ = combineLatest([this.appState.partRequest$, this.appState.partOffer$, this.auth.currentUser$.pipe(ensureTruthy())]).pipe(
    map(([request, offer, currentUser]) => ({ request, offer, currentUser })),
    shareReplay(1),
  );

  readonly currencyFormatter = currencyFormatter;
  readonly PartOfferState = PartOfferState;

  private documentsGridApi?: GridApi;
  public selectedVersion?: PartOffer;

  constructor(
    @Inject(DIALOG_DATA)
    readonly dialogData: { gridApi: GridApi },
    readonly appState: AppStateService,
    private readonly auth: AuthService,
    private readonly dialogRef: DialogRef,
    private readonly beData: BeDataService,
    private readonly dialog: DialogService,
  ) {
    super();
    combineLatest([this.appState.partOffer$, this.appState.partOfferVersions$])
      .pipe(takeUntil(this.destroyed$))
      .subscribe(([offer, versions]) => {
        this.selectedVersion = versions.find((version) => version.id === offer.id);
      });
  }

  cancel(): void {
    this.dialogRef.close(false);
  }

  autofill(value: any, form: FormGroup): void {
    Object.keys(value).forEach((key) => {
      if ((form.value.autofillEnabled && !form.value[key]) || (form.value[key] instanceof PartMasterData)) {
        form.patchValue({ [key]: value[key] });
      }
    });
  }

  fillFromRequest(offer: PartOffer, request: PartRequest): void {
    offer.getForm().patchValue({ ...request.getForm().value, id: offer.id, state: offer.state });
  }

  onVersionSelected(event: any): void {
    this.appState.partOfferContext$.next(event.value);
  }

  accept(): void {
    const dialogRef = this.dialog.open(PartOfferPaymentDialogComponent, {
      showHeader: false,
      style: {
        border: 'none',
        width: '600px',
      },
      contentStyle: { padding: 0 },
    });
    dialogRef.onClose.pipe(take(1)).subscribe((response) => {
      if (response) {
        this.save(PartOfferState.ACCEPTED);
      }
    });
  }

  reject(): void {
    const dialogRef = this.dialog.open(PartOfferCommentDialogComponent, {
      showHeader: false,
      style: {
        border: 'none',
        width: '60%',
      },
      contentStyle: { padding: 0 },
    });
    dialogRef.onClose.pipe(take(1)).subscribe((response) => {
      if (response) {
        this.save(PartOfferState.REJECTED);
      }
    });
  }

  confirmOrder(): void {
    const dialogRef = this.dialog.open(PartOfferOrderConfirmationDialogComponent, {
      showHeader: false,
      style: {
        border: 'none',
        width: '60%',
      },
      contentStyle: { padding: 0 },
    });
    dialogRef.onClose.pipe(take(1)).subscribe((response) => {
      if (response) {
        this.save(PartOfferState.ORDER_CONFIRMED);
      }
    });
  }

  save(state?: PartOfferState): void {
    combineLatest([this.appState.partRequest$, this.appState.partOffer$]).pipe(take(1)).subscribe(([partRequest, partOffer]) => {
      const dataToSave = partOffer.getDataToSave();
      dataToSave.state = state || dataToSave.state;
      dataToSave.requestId = partRequest.id;
      this.beData.savePartOffer$(dataToSave).pipe(takeUntil(this.destroyed$)).subscribe((savedOffer) => {
        this.appState.reloadRequests$.next();
        this.appState.reloadOffers$.next();
        this.appState.partOfferContext$.next(savedOffer);
      });
    });
  }

  saveAsVersion(state: PartOfferState): void {
    combineLatest([this.appState.partRequest$, this.appState.partOffer$]).pipe(take(1)).subscribe(([partRequest, partOffer]) => {
      const dataToSave = partOffer.getDataToSave();
      dataToSave.state = state;
      dataToSave.requestId = partRequest.id;
      dataToSave.previousVersionId = dataToSave.id;
      dataToSave.rootId = dataToSave.rootId;
      delete dataToSave.id;
      this.beData.savePartOffer$(dataToSave).pipe(takeUntil(this.destroyed$)).subscribe((savedOffer) => {
        this.appState.reloadRequests$.next();
        this.appState.reloadOffers$.next();
        this.appState.partOfferContext$.next(savedOffer);
      });
    });
  }

  onDocumentsGridReady(event: GridReadyEvent): void {
    this.documentsGridApi = event.api;
  }

  uploadDocuments(fileSelectEvent: any): void {
    if (fileSelectEvent.files) {
      forkJoin(this.getFiles(fileSelectEvent).map((file) => this.beData.uploadDocument$(file))).pipe(takeUntil(this.destroyed$)).subscribe((uploadedFiles) => {
        const offer = this.appState.partOfferContext$.value as PartOffer;
        offer.documents.push(...uploadedFiles);
        this.beData.savePartOffer$(offer.getDataToSave()).pipe(takeUntil(this.destroyed$)).subscribe((savedOffer) => {
          this.appState.partOfferContext$.next(savedOffer);
          this.documentsGridApi?.applyTransaction({ add: intersectionBy(savedOffer.documents, uploadedFiles, 'id') });
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
    const offer = this.appState.partOfferContext$.value as PartOffer;
    offer.documents.forEach((document, index) => {
      setTimeout(() => {
        window.location.href = document.url;
      }, index * 1000);
    });
  }

  removeDocument(file: BeFile): void {
    const offer = this.appState.partOfferContext$.value as PartOffer;
    const documentById = keyBy(offer.documents, 'id');
    delete documentById[file.id];
    offer.documents = Object.values(documentById);
    this.beData.savePartOffer$(offer.getDataToSave()).pipe(takeUntil(this.destroyed$)).subscribe((savedOffer) => {
      this.appState.partOfferContext$.next(savedOffer);
      this.documentsGridApi?.applyTransaction({ remove: [{ id: file.id }] });
    });
  }

}
