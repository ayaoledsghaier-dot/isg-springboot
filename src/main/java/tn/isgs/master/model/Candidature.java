package tn.isgs.master.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidatures", uniqueConstraints = @UniqueConstraint(columnNames = {"cin", "type_master"}))
@Getter @Setter
public class Candidature {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20) private String reference;
    @Column(nullable = false, length = 80) private String nom;
    @Column(nullable = false, length = 80) private String prenom;
    @Column(length = 160) private String nomAr;
    @Column(nullable = false, length = 20) private String cin;
    @Column(nullable = false) private LocalDate naissance;
    @Column(length = 80) private String lieu;
    @Column(length = 1) private String sexe;
    @Column(length = 40) private String nationalite;
    @Column(columnDefinition = "TEXT") private String adresse;
    @Column(length = 40) private String gouvernorat;
    @Column(length = 10) private String codePostal;
    @Column(nullable = false, length = 25) private String telephone;
    @Column(nullable = false, length = 120) private String email;

    private Integer bacAnnee;
    @Column(length = 60) private String bacSection;
    @Column(precision = 5, scale = 2) private BigDecimal bacMoyenne;
    @Column(length = 20) private String bacMention;
    @Column(length = 60) private String diplome;
    @Column(length = 120) private String specialite;
    @Column(length = 160) private String etablissement;
    private Integer licenceAnnee;
    @Column(precision = 5, scale = 2) private BigDecimal licenceMoyenne;
    @Column(length = 80) private String moyennesAns;
    @Column(length = 30) private String session;

    @Column(nullable = false, length = 40) private String typeMaster;
    @Column(nullable = false, length = 80) private String choix1;
    @Column(length = 80) private String choix2;
    @Column(length = 80) private String choix3;

    @Column(length = 20) private String niveauFr;
    @Column(length = 20) private String niveauEn;
    @Column(columnDefinition = "TEXT") private String stages;
    @Column(columnDefinition = "TEXT") private String experience;
    @Column(nullable = false, columnDefinition = "TEXT") private String motivation;

    @Column(nullable = false, length = 20) private String statut = "en attente";
    @Column(nullable = false) private LocalDateTime dateDepot = LocalDateTime.now();

    @OneToMany(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidatureFichier> fichiers = new ArrayList<>();
}
