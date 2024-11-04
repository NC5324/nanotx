package com.tusofia.ndurmush.business.partoffer.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.tusofia.ndurmush.base.entity.BaseEntity;
import com.tusofia.ndurmush.base.entity.LongIdentifiable;
import com.tusofia.ndurmush.base.entity.status.StatusAware;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.business.partoffer.PartOfferState;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_part_offer")
public class PartOffer extends BaseEntity implements StatusAware<PartOfferState>, LongIdentifiable {

    public static final String ID_SEQUENCE_NAME = "seq_part_offer_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPartOfferId")
    @SequenceGenerator(name = "seqPartOfferId", sequenceName = ID_SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "root_id")
    private Long rootId;

    @Column(name = "previous_version_id")
    private Long previousVersionId;

    @Column(name = "next_version_id")
    private Long nextVersionId;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "component_name")
    private String componentName;

    @Column(name = "category")
    private String category;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "alternate_part_number")
    private String alternatePartNumber;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "target_price")
    private Double targetPrice;

    @Column(name = "currency")
    private String currency;

    @Column(name = "min_temp")
    private Long minTemp;

    @Column(name = "max_temp")
    private Long maxTemp;

    @Column(name = "package_type")
    private String componentType;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private PartOfferState state;

    @Type(JsonType.class)
    @Column(name = "documents_metadata")
    private List<JsonNode> documents = new ArrayList<>();

    @Type(JsonType.class)
    @Column(name = "order_details")
    private JsonNode orderDetails;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    @Override
    public PartOfferState getState() {
        return state;
    }

    public void setState(PartOfferState state) {
        this.state = state;
    }

    public Long getPreviousVersionId() {
        return previousVersionId;
    }

    public void setPreviousVersionId(Long previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    public Long getNextVersionId() {
        return nextVersionId;
    }

    public void setNextVersionId(Long nextVersionId) {
        this.nextVersionId = nextVersionId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getAlternatePartNumber() {
        return alternatePartNumber;
    }

    public void setAlternatePartNumber(String alternatePartNumber) {
        this.alternatePartNumber = alternatePartNumber;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Long minTemp) {
        this.minTemp = minTemp;
    }

    public Long getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Long maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(final String componentType) {
        this.componentType = componentType;
    }

    public List<JsonNode> getDocuments() {
        return CollectionUtils.copy(documents);
    }

    public void setDocuments(List<JsonNode> documents) {
        this.documents = CollectionUtils.copy(documents);
    }

    public JsonNode getOrderDetails() {
        return orderDetails;
    }

    public PartOffer setOrderDetails(final JsonNode orderDetails) {
        this.orderDetails = orderDetails;
        return this;
    }
}
