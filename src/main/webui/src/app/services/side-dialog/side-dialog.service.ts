import { Dialog, DialogConfig, DialogRef } from '@angular/cdk/dialog';
import { ComponentType } from '@angular/cdk/overlay';
import { Injectable, TemplateRef } from '@angular/core';
import { SideDialogComponent } from 'src/app/components/side-dialog/side-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class SideDialogService extends Dialog {

  override open<R = unknown, D = unknown, C = unknown>(
    componentOrTemplateRef: ComponentType<C> | TemplateRef<C>,
    config?: DialogConfig<D, DialogRef<R, C>>
  ): DialogRef<R, C> {
    return super.open<R, D, C>(componentOrTemplateRef, {
      ...config,
      positionStrategy: this['_overlay'].position().global().end(),
      container: SideDialogComponent
    });
  }

}
