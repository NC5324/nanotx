import { ComponentType } from "./enums";
import { Serializable } from "./serializable.abstract";

export class PartMasterData extends Serializable {
    id!: number;
    componentName?: string;
    componentType?: ComponentType;
    category?: string;
    manufacturer?: string;
    partNumber?: string;
    alternatePartNumber?: string;
    minTemp?: number;
    maxTemp?: number;

    protected override reviveProperties(): void {
    }
}