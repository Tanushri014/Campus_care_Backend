package campus_care.repository;

import campus_care.entity.LostFound;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostFoundRepository
        extends JpaRepository<LostFound, Long> {

}