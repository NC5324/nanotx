import { ChangeDetectionStrategy, Component } from '@angular/core';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { BaseComponent } from 'src/app/components/base/base.component';
import { AppStateService } from '../../services/app-state/app-state.service';
import { map } from 'rxjs';
import { PartOfferComment } from 'src/app/models/part-offer-comment.class';
import { PartOffer } from 'src/app/models/part-offer.class';
import { BeDataService } from 'src/app/services/be-data/be-data.service';

@Component({
  selector: 'app-part-offer-comment-dialog',
  templateUrl: './part-offer-comment-dialog.component.html',
  styleUrls: ['./part-offer-comment-dialog.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PartOfferCommentDialogComponent extends BaseComponent {

  readonly comment = new PartOfferComment();

  constructor(
    readonly appState: AppStateService,
    private readonly beData: BeDataService,
    private readonly dialogRef: DynamicDialogRef
  ) {
    super();
  }

  cancel(): void {
    this.dialogRef.close();
  }

  save(): void {
    const offer = this.appState.partOfferContext$.value as PartOffer;
    this.comment.getForm().patchValue({ offerId: offer.id });
    this.beData.savePartOfferComment$(this.comment.getDataToSave()).subscribe((savedComment) => {
      this.appState.reloadPartOfferComments$.next();
      this.dialogRef.close(savedComment);
    });
  }

}
