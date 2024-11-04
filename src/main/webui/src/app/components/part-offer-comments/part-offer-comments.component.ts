import { ChangeDetectionStrategy, Component } from '@angular/core';
import { BaseComponent } from '../base/base.component';
import { AppStateService } from 'src/app/services/app-state/app-state.service';
import { PartOfferCommentDialogComponent } from 'src/app/dialogs/part-offer-comment-dialog/part-offer-comment-dialog.component';
import { DialogService } from 'primeng/dynamicdialog';
import { take } from 'rxjs';

@Component({
  selector: 'app-part-offer-comments',
  templateUrl: './part-offer-comments.component.html',
  styleUrls: ['./part-offer-comments.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PartOfferCommentsComponent extends BaseComponent {

  constructor(readonly appState: AppStateService, private readonly dialog: DialogService) {
    super();
  }

  viewOfferVersion(offerVersionId: number): void {
    this.appState.partOfferVersions$.pipe(take(1)).subscribe((offerVersions) => {
      const offerVersion = offerVersions.find((offer) => offer.id === offerVersionId);
      if (offerVersion) {
        this.appState.partOfferContext$.next(offerVersion.value);
        document.querySelector('.cdk-dialog-container .p-panel .p-panel-content')?.parentElement?.scroll(0, 0);
      }
    });
  }

  addComment(): void {
    this.dialog.open(PartOfferCommentDialogComponent, {
      showHeader: false,
      style: {
        border: 'none',
        width: '60%',
      },
      contentStyle: { padding: 0 },
    });
  }

}
