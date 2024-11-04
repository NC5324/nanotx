package com.tusofia.ndurmush.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public interface LongIdentifiable {
    Long getId();

    @JsonIgnore
    default boolean isExisting() {
        return Objects.nonNull(getId()) && getId() != 0L;
    }

    @JsonIgnore
    default boolean isNew() {
        return !isExisting();
    }
}
