package com.tusofia.ndurmush.business.document.facade;

import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.StringUtils;
import com.tusofia.ndurmush.business.document.entity.NtxDocument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@ApplicationScoped
public class NtxDocumentStorage {

    @ConfigProperty(name = "quarkus.http.body.uploads-directory")
    private String uploadsDirectory;

    @Inject
    private NtxDocumentFacade documentFacade;

    @Inject
    private EntityManager em;

    public String getRelativePath(final long id) {
        final StringBuilder sb = new StringBuilder();
        final long absoluteDocId = Math.abs(id);
        String.valueOf(absoluteDocId).chars().forEachOrdered(c -> sb.append((char) c).append("/"));
        sb.append(".").append(id < 0 ? "_" : "").append(absoluteDocId);
        return sb.toString();
    }

    public void writeToFile(final String relativePath, final InputStream is) throws IOException {
        File file = new File(getStorageRoot(), relativePath);
        FileUtils.copyInputStreamToFile(is, file);
    }

    public byte[] getContent(final NtxDocument document) throws IOException {
        File file = new File(getStorageRoot(), document.getFilePath());
        return FileUtils.readFileToByteArray(file);
    }

    public String getStorageRoot() throws IOException {
        return Files.createDirectories(Path.of(uploadsDirectory)).toString();
    }

    public void saveInDb(final String path) {
        File root = new File(path);
        File[] files = root.listFiles();
        for (File file : CollectionUtils.defaultList(files)) {
            if (file.isDirectory()) {
                saveInDb((file.getAbsolutePath()));
                continue;
            }
            saveInDb(file);
        }
    }

    private void saveInDb(final File file) {
        try {
            String fileName = StringUtils.trimLeadingChars(file.getName(), StringUtils.DOT);
            if (fileName.matches("\\d+")) {
                return;
            }
            Long docId = Long.parseLong(StringUtils.trimLeadingChars(file.getName(), StringUtils.DOT));
            NtxDocument document = documentFacade.load(docId);
            if (Objects.isNull(document)) {
                return;
            }
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            document.setContent(fileContent);
            documentFacade.save(document);
            em.flush();
            // TODO: Add logger
        } catch (final NumberFormatException e) {
            // TODO: Add logger
        } catch (final IOException e) {
            // TODO: Add logger
        }
    }

    public Optional<String> guessMimeType(final InputStream is, final String fileName) {
        try {
            String mimeType = null;
            final TikaConfig tika = new TikaConfig();
            final Metadata metadata = new Metadata();
            metadata.set("resourceName", fileName);
            final MediaType mediaType = tika.getDetector().detect(TikaInputStream.cast(is), metadata);
            if (Objects.nonNull(mediaType)) {
                mimeType = mediaType.toString();
            }
            if (isBlank(mimeType)) {
                mimeType = URLConnection.guessContentTypeFromStream(is);
            }
            if (isBlank(mimeType)) {
                mimeType = URLConnection.guessContentTypeFromName(fileName);
            }
            return Optional.ofNullable(mimeType);
        } catch (final IOException | TikaException ex) {
            // NO SONAR: Ignore
        }
        return Optional.empty();
    }
}