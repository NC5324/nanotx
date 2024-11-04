import { Serializable } from "./serializable.abstract";
import { User } from "./user.class";

export class BaseEntity extends Serializable {
    created?: Date;
    edited?: Date;
    creator?: User;
    editor?: User;
    deleted?: boolean;
    version?: number;

    public override getDataToSave() {
        const dataToSave = super.getDataToSave();
        Object.assign(dataToSave, this.getForm()?.value);
        delete dataToSave.creator;
        delete dataToSave.editor;
        delete dataToSave.created;
        delete dataToSave.edited;
        return dataToSave;
    }

    protected override reviveProperties(): void {
        this.reviveProperty(Date, ['created', 'edited']);
        this.reviveProperty(User, ['creator', 'editor']);
    }
}