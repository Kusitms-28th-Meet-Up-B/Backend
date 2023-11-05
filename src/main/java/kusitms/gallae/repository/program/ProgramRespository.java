package kusitms.gallae.repository.program;

import kusitms.gallae.domain.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramRespository extends JpaRepository<Program, Long> {
    @Override
    Optional<Program> findById(Long programId);

    Page<Program> findAllByProgramTypeOrderByCreatedAtDesc(String programType , Pageable pageable);

    Page<Program> findProgramByProgramNameContaining(String programName, Pageable pageable);

    List<Program> findTop4ByOrderByCreatedAtDesc();

    List<Program> findTop4ByOrderByProgramLikeDesc();
}
