package pl.mgis.problemreport.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mgis.problemreport.model.ReportStatus;

import java.util.Optional;

@JaversSpringDataAuditable
@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long> {

    @Query("select s from ReportStatus s where s.statusCode = :statusCode")
    Optional<ReportStatus> findByStatusCode(@Param(value = "statusCode") long statusCode);
}
