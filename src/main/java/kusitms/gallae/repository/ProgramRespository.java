package kusitms.gallae.repository;

import kusitms.gallae.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRespository extends JpaRepository<Program, Long> {
    @Override
    Optional<Program> findById(Long programId);

    List<Program> findByProgramType(String programType);

    List<Program> findTop4ByOrderByCreatedAtDesc();
}
