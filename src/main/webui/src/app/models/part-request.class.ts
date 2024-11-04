import { FormControl, FormGroup } from "@angular/forms";
import { BaseEntity } from "./base-entity.class";
import { PartOffer } from "./part-offer.class";
import { ComponentType, Currency } from "./enums";
import { snakeToUpperCaseSeparated } from "../utils/utils";
import { BeFile } from "./be-file.class";
import { User } from "./user.class";

export enum PartRequestState {
    OPEN = 'OPEN',
    IN_PROGRESS = 'IN_PROGRESS',
    WITHDRAWN = 'WITHDRAWN',
    CLOSED = 'CLOSED'
}

export const PART_REQUEST_STATE_COLOR_MAP: { [key in keyof typeof PartRequestState]: { background: string } } = {
    [PartRequestState.OPEN]: { background: '#DEE5EC' },
    [PartRequestState.IN_PROGRESS]: { background: '#DCE9EC' },
    [PartRequestState.WITHDRAWN]: { background: '#F2F2F2' },
    [PartRequestState.CLOSED]: { background: '#ECEECC' }
};

export class PartRequest extends BaseEntity {
    id!: number;
    componentName?: string;
    componentType?: ComponentType;
    category?: string;
    manufacturer?: string;
    partNumber?: string;
    alternatePartNumber?: string;
    quantity?: number;
    targetPrice?: number;
    currency?: Currency;
    minTemp?: number;
    maxTemp?: number;
    dynamicProperties?: any;
    state: PartRequestState = PartRequestState.OPEN;
    offers: PartOffer[] = [];
    documents: BeFile[] = [];

    get stateLabel(): string {
        return snakeToUpperCaseSeparated(this.state);
    }

    get stateBackground(): string {
        return PART_REQUEST_STATE_COLOR_MAP[this.state].background;
    }

    protected override reviveProperties(): void {
        super.reviveProperties();
        this.offers?.forEach((offer) => offer.partRequest = this);
        this.reviveProperty(PartOffer, 'offers');
        this.reviveProperty(BeFile, 'documents');
    }

    public override getForm(): FormGroup<any> {
        const form = super.getForm();
        form.addControl('id', new FormControl(this.id));
        form.addControl('autofillEnabled', new FormControl(true));
        form.addControl('componentName', new FormControl(this.componentName));
        form.addControl('componentType', new FormControl(this.componentType));
        form.addControl('category', new FormControl(this.category));
        form.addControl('manufacturer', new FormControl(this.manufacturer));
        form.addControl('partNumber', new FormControl(this.partNumber));
        form.addControl('alternatePartNumber', new FormControl(this.alternatePartNumber));
        form.addControl('quantity', new FormControl(this.quantity));
        form.addControl('targetPrice', new FormControl(this.targetPrice));
        form.addControl('currency', new FormControl(this.currency));
        form.addControl('minTemp', new FormControl(this.minTemp));
        form.addControl('maxTemp', new FormControl(this.maxTemp));
        form.addControl('state', new FormControl(this.state));
        form.addControl('documents', new FormControl(this.documents));
        return form;
    }

}