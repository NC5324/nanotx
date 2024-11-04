import { AbstractControl, ValidationErrors, Validators } from "@angular/forms";

export class FormValidators {

    static greaterThan = (controlToCompareName: string) => (control: AbstractControl): ValidationErrors | null => {
        const controlToCompare = control.root?.get(controlToCompareName);
        if (controlToCompare?.value > control.value) {
            return { lessThan: true };
        }
        return null;
    };

    static requiredLength = (requiredLength: number) => (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;
        if (value && !Array.isArray(value)) {
            throw new Error('Invalid control value!');
        }
        if (!control.value?.length || (control.value.length < requiredLength)) {
            return { invalidLength: true };
        }
        return null;
    };

    static some = (condition: (v: any) => boolean) => (control: AbstractControl): ValidationErrors | null => {
        const value = control.value;
        if (value && !Array.isArray(value)) {
            throw new Error('Invalid control value!');
        }
        if (!value.some((v: any) => condition(v))) {
            return { none: true };
        }
        return null;
    };

}