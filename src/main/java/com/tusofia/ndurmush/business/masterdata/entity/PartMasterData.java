package com.tusofia.ndurmush.business.masterdata.entity;

import com.tusofia.ndurmush.base.entity.LongIdentifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_master_data")
public class PartMasterData implements LongIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPartOfferId")
    @SequenceGenerator(name = "seqPartOfferId", sequenceName = "seq_part_offer_id", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

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

    @Column(name = "min_temp")
    private Long minTemp;

    @Column(name = "max_temp")
    private Long maxTemp;

    @Column(name = "type")
    private String componentType;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
