package pl.mgis.problemreport.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mgis.problemreport.model.ReportType;

@JaversSpringDataAuditable
@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {
}
