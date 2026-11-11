package tn.isgs.master.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "candidature_fichiers")
@Getter @Setter
public class CandidatureFichier {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidature_id")
    private Candidature candidature;

    @Column(nullable = false, length = 30)  private String typePiece;   // photo, cin, cv, bac, lic, notes
    @Column(nullable = false)               private String nomFichier;
    @Column(nullable = false, length = 400) private String chemin;
}
