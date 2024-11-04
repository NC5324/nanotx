import { ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Subject, BehaviorSubject, map, Observable, shareReplay, combineLatest } from 'rxjs';
import { BaseComponent } from '../base/base.component';

export type SideMenuItem = MenuItem & { align?: 'start' | 'end' }

@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.sass'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SideMenuComponent extends BaseComponent {

  @Input() menuItems: SideMenuItem[] = [];

  readonly menuExpanded$ = new BehaviorSubject<boolean>(false);
  
  readonly menuItems$: Observable<SideMenuItem[]> = combineLatest([this.menuExpanded$, this.observe$('menuItems')]).pipe(
    map(([menuExpanded]) => {
      const menuItems: SideMenuItem[] = [
        ...this.menuItems.map((menuItem) => ({ ...menuItem })),
        {
          icon: menuExpanded ? 'pi-angle-left' : 'pi-angle-right',
          command: () => this.toggleMenu(),
          label: 'Minimize',
          align: 'end',
        }
      ];
      if (!menuExpanded) {
        menuItems.forEach((menuItem) => delete menuItem.label);
      }
      return menuItems;
    }),
    shareReplay(1),
  );

  constructor() {
    super();
  }

  toggleMenu(): void {
    this.menuExpanded$.next(!this.menuExpanded$.value);
  }

  onClick(menuItem: SideMenuItem): void {
    if (menuItem.command) {
      menuItem.command();
    }
  }

}
