package pl.mgis.problemreport.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mgis.problemreport.model.Report;

import java.util.List;
import java.util.Optional;

@JaversSpringDataAuditable
@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    @Query("Select r From Report r")
    List<Report> findAllReports(Pageable pageable);


    @Query(value="SELECT * FROM reports ORDER BY ID ?#{:order} LIMIT :start,:length", nativeQuery = true)
    List<Report> findReportsByPage( @Param(value = "start") int start, @Param(value = "length")int length ,
                                    @Param(value = "order") Sort.Direction order );

    @Query(value = "SELECT r FROM Report r "
            + "left join ReportType t on r.reportType=t.id "
            + "left join ReportStatus s on r.reportStatus=s.id "
            + "WHERE ( :inputString is null or "
            + "lower(concat(r.id, ' ', s.description, ' ',  t.description, ' ', r.message, ' ', r.email, ' ')) like %:inputString% )"
    )
    List<Report> findReportsByString(@Param(value = "inputString") String inputString, Pageable pageable);


    @Query(value="SELECT count(*) FROM reports", nativeQuery = true)
    int totalReports();

    @Query(value="SELECT * FROM reports where uuid = :uuidString", nativeQuery = true)
    Optional<Report> findReportByUuid(@Param(value = "uuidString") String uuidString);

    List<Report> findAllReportsByEmail(String emailAddress);
}
