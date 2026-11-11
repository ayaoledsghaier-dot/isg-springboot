package tn.isgs.master.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Les noms des composants correspondent aux attributs name="" du formulaire HTML. */
public record CandidatureForm(
        @NotBlank(message = "Le nom est obligatoire.") String nom,
        @NotBlank(message = "Le prénom est obligatoire.") String prenom,
        String nom_ar,
        @NotBlank(message = "Le CIN est obligatoire.") String cin,
        @NotNull(message = "La date de naissance est obligatoire.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate naissance,
        String lieu, String sexe, String nat, String adresse, String gouv, String cp,
        @NotBlank(message = "Le téléphone est obligatoire.") String tel,
        @NotBlank(message = "L'e-mail est obligatoire.") @Email(message = "Adresse e-mail invalide.") String email,

        Integer bac_an, String bac_sec,
        @DecimalMin(value = "0", message = "Moyenne du Bac invalide.") @DecimalMax(value = "20", message = "Moyenne du Bac invalide.") BigDecimal bac_moy,
        String bac_men, String dipl, String spec, String etab, Integer lic_an,
        @DecimalMin(value = "0", message = "Moyenne de licence invalide.") @DecimalMax(value = "20", message = "Moyenne de licence invalide.") BigDecimal lic_moy,
        String moy_ann, String session,

        @NotBlank(message = "Le type de master est obligatoire.") String type,
        @NotBlank(message = "Le premier choix est obligatoire.") String c1,
        String c2, String c3,

        String fr, String en, String stages, String exp,
        @NotBlank(message = "La lettre de motivation est obligatoire.") String motiv,

        String website,                       // champ anti-robot (doit rester vide)

        MultipartFile f_photo, MultipartFile f_cin, MultipartFile f_cv,
        MultipartFile f_bac, MultipartFile f_lic, List<MultipartFile> f_notes
) {}
