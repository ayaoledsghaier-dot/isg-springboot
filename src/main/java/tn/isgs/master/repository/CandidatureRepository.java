package tn.isgs.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isgs.master.model.Candidature;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    boolean existsByCinAndTypeMaster(String cin, String typeMaster);
}
