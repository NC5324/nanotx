package com.tusofia.ndurmush.business.partoffer.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.tusofia.ndurmush.base.entity.BaseEntity;
import com.tusofia.ndurmush.base.entity.LongIdentifiable;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_part_offer_comment")
public class PartOfferComment extends BaseEntity implements LongIdentifiable {

    @Id
    @SequenceGenerator(name = "seqPartOfferCommentId", sequenceName = "seq_part_offer_comment_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPartOfferCommentId")
    @Column(name = "id")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "content")
    private String content;

    @Column(name = "offer_id")
    private Long offerId;

    @Type(JsonType.class)
    @Column(name = "documents_metadata")
    private List<JsonNode> documents = new ArrayList<>();

    @Transient
    private PartOffer offer;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public List<JsonNode> getDocuments() {
        return CollectionUtils.copy(documents);
    }

    public void setDocuments(List<JsonNode> documents) {
        this.documents = CollectionUtils.copy(documents);
    }

    public PartOffer getOffer() {
        return offer;
    }

    public void setOffer(final PartOffer offer) {
        this.offer = offer;
    }
}
