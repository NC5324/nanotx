package com.tusofia.ndurmush.base.entity.status;

public interface StatusAware<T extends Enum<T>> {
    T getState();
}
