package tn.isgs.master.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import tn.isgs.master.dto.CandidatureForm;
import tn.isgs.master.service.CandidatureService;
import java.util.Map;

@RestController
@RequestMapping("/api/candidatures")
public class CandidatureController {
    private final CandidatureService service;

    public CandidatureController(CandidatureService service) { this.service = service; }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> deposer(@Valid @ModelAttribute CandidatureForm form,
                                                       BindingResult erreurs) throws MessagingException {
        // champ caché rempli = robot : on répond OK sans rien faire
        if (form.website() != null && !form.website().isBlank())
            return ResponseEntity.ok(Map.of("ok", true, "message", "Merci.", "ref", "-"));

        if (erreurs.hasErrors()) {
            FieldError fe = erreurs.getFieldErrors().get(0);
            String m = fe.getDefaultMessage() != null && !fe.getDefaultMessage().contains("Failed to convert")
                    ? fe.getDefaultMessage() : "Le champ « " + fe.getField() + " » est invalide.";
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", m));
        }

        String ref = service.deposer(form);
        return ResponseEntity.ok(Map.of("ok", true,
                "message", "Votre candidature a bien été envoyée. Un e-mail de confirmation vous a été adressé.",
                "ref", ref));
    }
}
