package tn.isgs.master.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;

/** Enregistre les pièces jointes dans le dossier "uploads" (hors du dossier public). */
@Service
public class FileStorageService {
    private static final long MAX = 5L * 1024 * 1024;
    private static final Set<String> EXT = Set.of("pdf", "jpg", "jpeg", "png");
    private final Path dir;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(dir);
    }

    public void check(MultipartFile f, String label) {
        if (f == null || f.isEmpty()) throw new BusinessException("La pièce « " + label + " » est obligatoire.");
        if (f.getSize() > MAX) throw new BusinessException("Fichier trop volumineux : " + f.getOriginalFilename() + " (5 Mo maximum).");
        String name = f.getOriginalFilename() == null ? "" : f.getOriginalFilename();
        String ext = name.contains(".") ? name.substring(name.lastIndexOf('.') + 1).toLowerCase() : "";
        if (!EXT.contains(ext)) throw new BusinessException("Format non accepté : " + name + " (PDF, JPG ou PNG).");
    }

    public Path save(MultipartFile f, String reference) {
        String clean = String.valueOf(f.getOriginalFilename()).replaceAll("[^A-Za-z0-9._-]", "_");
        Path target = dir.resolve(reference + "_" + clean).normalize();
        if (!target.startsWith(dir)) throw new BusinessException("Nom de fichier invalide.");
        try {
            f.transferTo(target);
            return target;
        } catch (IOException e) {
            throw new IllegalStateException("Impossible d'enregistrer le fichier.", e);
        }
    }
}
