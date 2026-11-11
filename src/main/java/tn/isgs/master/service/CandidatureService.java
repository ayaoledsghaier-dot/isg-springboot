package tn.isgs.master.service;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.isgs.master.dto.CandidatureForm;
import tn.isgs.master.model.Candidature;
import tn.isgs.master.model.CandidatureFichier;
import tn.isgs.master.repository.CandidatureRepository;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class CandidatureService {
    private final CandidatureRepository repo;
    private final FileStorageService storage;
    private final MailService mail;
    private final SecureRandom rnd = new SecureRandom();

    public CandidatureService(CandidatureRepository repo, FileStorageService storage, MailService mail) {
        this.repo = repo; this.storage = storage; this.mail = mail;
    }

    /** Tout est dans une transaction : si l'e-mail échoue, l'enregistrement est annulé. */
    @Transactional(rollbackFor = Exception.class)
    public String deposer(CandidatureForm f) throws MessagingException {
        if (repo.existsByCinAndTypeMaster(f.cin().trim(), f.type()))
            throw new BusinessException("Une candidature existe déjà pour ce CIN et ce type de master.", HttpStatus.CONFLICT);

        // 1) Vérifier les pièces avant d'écrire quoi que ce soit
        storage.check(f.f_photo(), "Photo d'identité");
        storage.check(f.f_cin(), "Copie de la CIN");
        storage.check(f.f_cv(), "CV");
        storage.check(f.f_bac(), "Diplôme du Bac");
        storage.check(f.f_lic(), "Diplôme de licence");
        if (f.f_notes() == null || f.f_notes().stream().allMatch(MultipartFile::isEmpty))
            throw new BusinessException("Les relevés de notes sont obligatoires.");
        f.f_notes().forEach(n -> { if (!n.isEmpty()) storage.check(n, "Relevés de notes"); });

        // 2) Candidature
        Candidature c = new Candidature();
        c.setReference("MST-" + Year.now() + "-" + String.format("%06X", rnd.nextInt(0xFFFFFF)));
        c.setNom(f.nom().trim()); c.setPrenom(f.prenom().trim()); c.setNomAr(vide(f.nom_ar()));
        c.setCin(f.cin().trim()); c.setNaissance(f.naissance()); c.setLieu(vide(f.lieu()));
        c.setSexe(vide(f.sexe())); c.setNationalite(vide(f.nat())); c.setAdresse(vide(f.adresse()));
        c.setGouvernorat(vide(f.gouv())); c.setCodePostal(vide(f.cp()));
        c.setTelephone(f.tel().trim()); c.setEmail(f.email().trim());
        c.setBacAnnee(f.bac_an()); c.setBacSection(vide(f.bac_sec())); c.setBacMoyenne(f.bac_moy()); c.setBacMention(vide(f.bac_men()));
        c.setDiplome(vide(f.dipl())); c.setSpecialite(vide(f.spec())); c.setEtablissement(vide(f.etab()));
        c.setLicenceAnnee(f.lic_an()); c.setLicenceMoyenne(f.lic_moy()); c.setMoyennesAns(vide(f.moy_ann())); c.setSession(vide(f.session()));
        c.setTypeMaster(f.type()); c.setChoix1(f.c1()); c.setChoix2(vide(f.c2())); c.setChoix3(vide(f.c3()));
        c.setNiveauFr(vide(f.fr())); c.setNiveauEn(vide(f.en()));
        c.setStages(vide(f.stages())); c.setExperience(vide(f.exp())); c.setMotivation(f.motiv().trim());

        // 3) Fichiers
        List<Path> pieces = new ArrayList<>();
        ajouter(c, pieces, f.f_photo(), "photo");
        ajouter(c, pieces, f.f_cin(), "cin");
        ajouter(c, pieces, f.f_cv(), "cv");
        ajouter(c, pieces, f.f_bac(), "bac");
        ajouter(c, pieces, f.f_lic(), "licence");
        for (MultipartFile n : f.f_notes()) if (!n.isEmpty()) ajouter(c, pieces, n, "notes");

        repo.save(c);

        // 4) E-mails
        mail.envoyerAdmin(c, pieces);
        mail.envoyerAccuse(c);
        return c.getReference();
    }

    private void ajouter(Candidature c, List<Path> pieces, MultipartFile file, String type) {
        Path p = storage.save(file, c.getReference());
        pieces.add(p);
        CandidatureFichier cf = new CandidatureFichier();
        cf.setCandidature(c); cf.setTypePiece(type);
        cf.setNomFichier(file.getOriginalFilename()); cf.setChemin(p.toString());
        c.getFichiers().add(cf);
    }

    private static String vide(String s) { return s == null || s.isBlank() ? null : s.trim(); }
}
