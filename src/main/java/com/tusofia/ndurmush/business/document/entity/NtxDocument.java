package com.tusofia.ndurmush.business.document.entity;

import com.tusofia.ndurmush.base.entity.BaseEntity;
import com.tusofia.ndurmush.base.entity.LongIdentifiable;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_document")
public class NtxDocument extends BaseEntity implements LongIdentifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSblDocumentId")
    @SequenceGenerator(name = "seqSblDocumentId", sequenceName = "seq_document_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content")
    private byte[] content;

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getContent() {
        return CollectionUtils.copy(this.content);
    }

    public void setContent(byte[] content) {
        this.content = CollectionUtils.copy(content);
    }

}
