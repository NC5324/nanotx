import { API_BASE_URL } from '../utils/constants';
import { Serializable } from './serializable.abstract';

export class BeFile extends Serializable {
    id!: number;
    name!: string;
    mimetype!: string;
    size!: number;
    created!: Date;
    creator!: string;

    get url(): string {
      return `${API_BASE_URL}/document/${this.id}`;
    }

    public override getDataToSave() {
      const dataToSave = super.getDataToSave();
      Object.assign(dataToSave, this);
      delete dataToSave.tsTemplate;
      return dataToSave;
    }

    protected override reviveProperties(): void {
        this.reviveProperty(Date, 'created');
    }
}