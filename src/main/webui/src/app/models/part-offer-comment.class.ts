import { FormControl, FormGroup } from "@angular/forms";
import { BaseEntity } from "./base-entity.class";
import { PartOffer } from "./part-offer.class";

export class PartOfferComment extends BaseEntity {
    id!: number;
    parentId?: number;
    offerId!: number;
    offerRootId!: number;
    content?: string;
    offer?: PartOffer;

    protected override reviveProperties(): void {
        super.reviveProperties();
        this.reviveProperty(PartOffer, 'offer');
    }

    public override getForm(): FormGroup<any> {
        const form = super.getForm();
        form.addControl('id', new FormControl(this.id));
        form.addControl('parentId', new FormControl(this.parentId));
        form.addControl('offerId', new FormControl(this.offerId));
        form.addControl('offerRootId', new FormControl(this.offerRootId));
        form.addControl('content', new FormControl(this.content));
        return form;
    }
}