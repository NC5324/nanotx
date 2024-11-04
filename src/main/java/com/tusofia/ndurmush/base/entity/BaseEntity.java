package com.tusofia.ndurmush.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tusofia.ndurmush.business.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "edited")
    @Temporal(TemporalType.TIMESTAMP)
    private Date edited;

    @ManyToOne
    @JoinColumn(name = "creator")
    @JsonIgnoreProperties({"role", "creator", "editor", "created", "edited", "version", "deleted"})
    private User creator;

    @ManyToOne
    @JoinColumn(name = "editor")
    @JsonIgnoreProperties({"role", "creator", "editor", "created", "edited", "version", "deleted"})
    private User editor;

    @Column(name = "version")
    private long version;

    @Column(name = "deleted")
    private boolean deleted = false;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getEdited() {
        return edited;
    }

    public void setEdited(Date edited) {
        this.edited = edited;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
