import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Serializable } from "./serializable.abstract";

export class User extends Serializable {
    id!: number;
    login!: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    phoneNumber?: string;
    role?: UserRole;
    password?: string;

    get isBuyer(): boolean {
        return this.role === UserRole.BUYER;
    }

    get isSupplier(): boolean {
        return this.role === UserRole.SUPPLIER;
    }

    get isAdmin(): boolean {
        return this.role === UserRole.ADMIN;
    }

    get displayName(): string {
        return `${this.firstName} ${this.lastName}`;
    }

    protected override reviveProperties(): void {
    }

    public override getForm(): FormGroup<any> {
        const form = super.getForm();
        form.addControl('login', new FormControl(this.login, Validators.required));
        form.addControl('firstName', new FormControl(this.firstName, Validators.required));
        form.addControl('lastName', new FormControl(this.lastName, Validators.required));
        form.addControl('email', new FormControl(this.email, [Validators.required, Validators.email]));
        form.addControl('phoneNumber', new FormControl(this.phoneNumber, Validators.required));
        form.addControl('role', new FormControl(this.role, Validators.required));
        return form;
    }

    public getRegisterForm(): FormGroup<any> {
        const form = this.getForm();
        form.addControl('password', new FormControl(this.password, Validators.required));
        form.addControl('confirmPassword', new FormControl(null, Validators.required));
        form.addControl('confirmEmail', new FormControl(null, [Validators.required, Validators.email]));
        return form;
    }

    public override getDataToSave(): any {
        const dataToSave = Object.assign({}, this.form?.value);
        return dataToSave;
    }
}

export enum UserRole {
    ADMIN = 'ADMIN',
    BUYER = 'BUYER',
    SUPPLIER = 'SUPPLIER'
}