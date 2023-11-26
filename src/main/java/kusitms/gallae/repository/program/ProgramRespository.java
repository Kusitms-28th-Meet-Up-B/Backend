package kusitms.gallae.repository.program;

import jakarta.persistence.LockModeType;
import kusitms.gallae.domain.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramRespository extends JpaRepository<Program, Long> {
    @Override
    Optional<Program> findById(Long programId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Program p where p.id = :programId")
    Optional<Program> findWithIdForUpdate(@Param("programId") Long programId);

    Program findByUserIdAndStatus(Long id, Program.ProgramStatus programStatus);

    List<Program> findTop4ByLocationContainingAndProgramTypeContainingAndStatus(String Location, String programType
            , Program.ProgramStatus programStatus);

    void deleteByIdAndStatus(Long programId,Program.ProgramStatus programStatus);

}
