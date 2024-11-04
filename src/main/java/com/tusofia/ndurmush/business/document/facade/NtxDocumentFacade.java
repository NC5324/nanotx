package com.tusofia.ndurmush.business.document.facade;

import com.tusofia.ndurmush.base.facade.BaseDbFacade;
import com.tusofia.ndurmush.business.document.entity.NtxDocument;
import com.tusofia.ndurmush.business.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Objects;

@ApplicationScoped
public class NtxDocumentFacade extends BaseDbFacade {

    @Inject
    private NtxDocumentStorage documentStorage;

    public NtxDocument createFile(final InputStream is, final String fileName, final boolean saveInDb, final User user) throws IOException {
        NtxDocument document = new NtxDocument();
        byte[] byteArray = IOUtils.toByteArray(is);
        document.setFileName(fileName);
        document.setMimeType(documentStorage.guessMimeType(is, fileName).orElse(null));
        if (saveInDb) {
            document.setContent(byteArray);
        } else {
            String relativePath = documentStorage.getRelativePath(document.getId());
            documentStorage.writeToFile(relativePath, is);
            document.setFilePath(relativePath);
        }
        document.setFileSize((long) byteArray.length);
        document = save(document, user);
        IOUtils.closeQuietly(is);
        return document;
    }

    public Response download(final Long id) {
        NtxDocument document = load(id);
        if (Objects.isNull(document)) {
            return Response.status(Response.Status.NOT_FOUND).entity(String.format("File with ID: %s does not exist!", id)).build();
        }
        String contentDisposition = "attachment; filename*=UTF-8''" + URLEncoder.encode(document.getFileName(), StandardCharsets.UTF_8);
        return Response.ok(document.getContent()).type(document.getMimeType()).header("content-disposition", contentDisposition).build();
    }

    public NtxDocument load(final Long id) {
        return load(id, NtxDocument.class, true);
    }

    public NtxDocument save(final NtxDocument document) {
        return save(document, null);
    }

    public NtxDocument save(final NtxDocument document, final User user) {
        return save(document, user, document.isNew());
    }

    public String buildMetaDataString(final NtxDocument file) {
        JsonObjectBuilder metaDataBuilder = Json.createObjectBuilder();
        if (Objects.nonNull(file)) {
            metaDataBuilder.add("id", file.getId());
            metaDataBuilder.add("name", file.getFileName());
            metaDataBuilder.add("size", file.getFileSize());
            metaDataBuilder.add("mimetype", file.getMimeType());
            metaDataBuilder.add("creator", file.getCreator().getReversedDisplayName());
            metaDataBuilder.add("created", new SimpleDateFormat("MMM d, yyyy, hh:mm:ss aaa z").format(file.getCreated()));
        }
        return metaDataBuilder.build().toString();
    }

}
