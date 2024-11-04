import { FormGroup, FormControl } from "@angular/forms";
import { BaseEntity } from "./base-entity.class";
import { PartOfferComment } from "./part-offer-comment.class";
import { ComponentType } from "ag-grid-community";
import { PartRequest } from "./part-request.class";
import { snakeToUpperCaseSeparated } from "../utils/utils";
import { BeFile } from "./be-file.class";
import { User } from "./user.class";
import { Currency } from "./enums";

export enum PartOfferState {
    OPEN = 'OPEN',
    SUBMITTED = 'SUBMITTED',
    WITHDRAWN = 'WITHDRAWN',
    REJECTED = 'REJECTED',
    ACCEPTED = 'ACCEPTED',
    ORDER_CONFIRMED = 'ORDER_CONFIRMED',
    ORDER_COMPLETED = 'ORDER_COMPLETED'
}

export const PART_OFFER_STATE_COLOR_MAP: { [key in keyof typeof PartOfferState]: { background: string } } = {
    [PartOfferState.OPEN]: { background: '#DEE5EC' },
    [PartOfferState.SUBMITTED]: { background: '#DCE9EC' },
    [PartOfferState.WITHDRAWN]: { background: '#F2F2F2' },
    [PartOfferState.REJECTED]: { background: '#EABEBF' },
    [PartOfferState.ACCEPTED]: { background: '#ECEECC' },
    [PartOfferState.ORDER_CONFIRMED]: { background: '#ECEECC' },
    [PartOfferState.ORDER_COMPLETED]: { background: '#ECEECC' }
};

export class PartOffer extends BaseEntity {
    id!: number;
    rootId!: number;
    previousVersionId?: number;
    nextVersionId?: number;
    requestId!: number;
    partRequest?: PartRequest;
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
    dynamicProperties?: string;
    state: PartOfferState = PartOfferState.OPEN;
    comments: PartOfferComment[] = [];
    documents: BeFile[] = [];
    orderDetails?: any;

    get stateLabel(): string {
        return snakeToUpperCaseSeparated(this.state);
    }

    get stateBackground(): string {
        return PART_OFFER_STATE_COLOR_MAP[this.state].background;
    }

    protected override reviveProperties(): void {
        super.reviveProperties();
        this.reviveProperty(PartRequest, 'partRequest');
        this.reviveProperty(PartOfferComment, 'comments');
        this.reviveProperty(BeFile, 'documents');
    }

    public override getForm(): FormGroup<any> {
        const form = super.getForm();
        form.addControl('id', new FormControl(this.id));
        form.addControl('autofillEnabled', new FormControl(true));
        form.addControl('rootId', new FormControl(this.rootId));
        form.addControl('requestId', new FormControl(this.requestId));
        form.addControl('previousVersionId', new FormControl(this.previousVersionId));
        form.addControl('nextVersionId', new FormControl(this.nextVersionId));
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
        form.addControl('orderDetails', new FormControl(this.orderDetails));
        return form;
    }

    public override getDataToSave() {
        const dataToSave = super.getDataToSave();
        dataToSave.documents = this.documents.map((document) => document.getDataToSave());
        delete dataToSave.partRequest;
        return dataToSave;
    }

    public canSetState(state: PartOfferState, user: User): boolean {
        return this.isStateValid(state) && this.isStateAuthorized(state, user);
    }

    private isStateValid(state: PartOfferState): boolean {
        if (this.nextVersionId) {
            return false;
        }
        switch (this.state) {
            case PartOfferState.OPEN:
                return !state || state === PartOfferState.OPEN || state === PartOfferState.SUBMITTED;
            case PartOfferState.SUBMITTED:
                return state === PartOfferState.ACCEPTED || state === PartOfferState.REJECTED || state === PartOfferState.WITHDRAWN;
            case PartOfferState.WITHDRAWN:
                return state === PartOfferState.OPEN;
            case PartOfferState.ACCEPTED:
                return state === PartOfferState.ORDER_CONFIRMED || state === PartOfferState.WITHDRAWN;
            case PartOfferState.REJECTED:
                return state === PartOfferState.OPEN;
            case PartOfferState.ORDER_CONFIRMED:
                return state === PartOfferState.ORDER_COMPLETED;
            default:
                return false;
        }
    }

    private isStateAuthorized(state: PartOfferState, user: User): boolean {
        if (user.isAdmin) {
            return true;
        }
        switch (state) {
            case PartOfferState.OPEN:
            case PartOfferState.SUBMITTED:
            case PartOfferState.ORDER_CONFIRMED:
            case PartOfferState.WITHDRAWN:
                return user.isSupplier;
            case PartOfferState.ACCEPTED:
            case PartOfferState.REJECTED:
            case PartOfferState.ORDER_COMPLETED:
                return user.isBuyer;
            default:
                return false;
        }
    }

}