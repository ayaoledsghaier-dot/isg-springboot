package tn.isgs.master.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import tn.isgs.master.model.Candidature;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailService {
    private final JavaMailSender sender;
    private final String from;
    private final String dest;

    public MailService(JavaMailSender sender,
                       @Value("${spring.mail.username}") String from,
                       @Value("${app.mail.destinataire}") String dest) {
        this.sender = sender; this.from = from; this.dest = dest;
    }

    /** E-mail à l'administration : récapitulatif + pièces jointes. */
    public void envoyerAdmin(Candidature c, List<Path> pieces) throws MessagingException {
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
        try { h.setFrom(from, "ISG Sousse - Candidatures"); } catch (Exception e) { h.setFrom(from); }
        h.setTo(dest);
        h.setReplyTo(c.getEmail());
        h.setSubject("[Master] Nouvelle candidature " + c.getReference() + " - " + c.getNom() + " " + c.getPrenom());
        h.setText(layout("Nouvelle candidature en Master · Réf. " + c.getReference(),
                "<p>Candidature déposée le " + java.time.LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm")) + ".</p>" + tableau(c)), true);
        for (Path p : pieces) h.addAttachment(p.getFileName().toString(), new FileSystemResource(p));
        sender.send(msg);
    }

    /** Accusé de réception au candidat. */
    public void envoyerAccuse(Candidature c) throws MessagingException {
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(msg, true, "UTF-8");
        try { h.setFrom(from, "ISG Sousse - Admission"); } catch (Exception e) { h.setFrom(from); }
        h.setTo(c.getEmail());
        h.setSubject("Accusé de réception de votre candidature - Réf. " + c.getReference());
        h.setText(layout("Accusé de réception",
                "<p>Bonjour " + e(c.getPrenom()) + " " + e(c.getNom()) + ",</p>"
              + "<p>Nous avons bien reçu votre candidature au Master <b>" + e(c.getChoix1()) + "</b>.</p>"
              + "<p>Votre numéro de dossier : <b>" + c.getReference() + "</b>. Merci de le conserver.</p>"
              + "<p>Votre dossier sera étudié par la commission d'admission. Vous serez informé(e) par e-mail.</p>"
              + "<p>Cordialement,<br><b>Service de la scolarité - ISG Sousse</b></p>"), true);
        sender.send(msg);
    }

    private String tableau(Candidature c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("Nom", c.getNom()); m.put("Prénom", c.getPrenom()); m.put("Nom en arabe", c.getNomAr());
        m.put("CIN / Passeport", c.getCin()); m.put("Date de naissance", c.getNaissance());
        m.put("Lieu de naissance", c.getLieu()); m.put("Sexe", c.getSexe()); m.put("Nationalité", c.getNationalite());
        m.put("Adresse", c.getAdresse()); m.put("Gouvernorat", c.getGouvernorat()); m.put("Code postal", c.getCodePostal());
        m.put("Téléphone", c.getTelephone()); m.put("E-mail", c.getEmail());
        m.put("Année du Bac", c.getBacAnnee()); m.put("Section du Bac", c.getBacSection());
        m.put("Moyenne du Bac", c.getBacMoyenne()); m.put("Mention du Bac", c.getBacMention());
        m.put("Diplôme", c.getDiplome()); m.put("Spécialité", c.getSpecialite()); m.put("Établissement", c.getEtablissement());
        m.put("Année de licence", c.getLicenceAnnee()); m.put("Moyenne de licence", c.getLicenceMoyenne());
        m.put("Moyennes L1/L2/L3", c.getMoyennesAns()); m.put("Session", c.getSession());
        m.put("Type de Master", c.getTypeMaster()); m.put("Choix 1", c.getChoix1());
        m.put("Choix 2", c.getChoix2()); m.put("Choix 3", c.getChoix3());
        m.put("Français", c.getNiveauFr()); m.put("Anglais", c.getNiveauEn());
        m.put("Stages", c.getStages()); m.put("Expérience", c.getExperience());
        m.put("Lettre de motivation", c.getMotivation());
        StringBuilder sb = new StringBuilder("<table style='width:100%;border-collapse:collapse;font-size:13.5px'>");
        m.forEach((k, v) -> sb.append("<tr><td style='padding:8px 12px;background:#f3f6fa;font-weight:600;width:35%;border-bottom:1px solid #e3e8ee'>")
                .append(k).append("</td><td style='padding:8px 12px;border-bottom:1px solid #e3e8ee'>")
                .append(v == null || v.toString().isBlank() ? "<span style='color:#999'>—</span>" : e(v.toString()).replace("\n", "<br>"))
                .append("</td></tr>"));
        return sb.append("</table>").toString();
    }

    private static String e(String s) { return HtmlUtils.htmlEscape(s == null ? "" : s); }

    private static String layout(String titre, String corps) {
        return "<div style='font-family:Arial,sans-serif;background:#eef2f6;padding:24px'>"
             + "<div style='max-width:680px;margin:auto;background:#fff;border-radius:8px;overflow:hidden'>"
             + "<div style='background:#0d6fb8;color:#fff;padding:18px 24px'>"
             + "<div style='font-size:18px;font-weight:bold'>Institut Supérieur de Gestion de Sousse</div>"
             + "<div style='font-size:13px;opacity:.9'>" + titre + "</div></div>"
             + "<div style='padding:24px;color:#223;font-size:14px;line-height:1.6'>" + corps + "</div>"
             + "<div style='background:#f3f6fa;padding:14px 24px;font-size:12px;color:#667'>"
             + "ISG Sousse · Ce message est généré automatiquement.</div></div></div>";
    }
}
