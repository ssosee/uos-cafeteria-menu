package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.StudentHall;

import java.util.Optional;

public interface StudentHallRepository extends JpaRepository<StudentHall, Long> {

}
