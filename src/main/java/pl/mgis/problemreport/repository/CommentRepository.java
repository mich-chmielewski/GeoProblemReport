package pl.mgis.problemreport.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.mgis.problemreport.model.Comment;

import java.util.List;

@JaversSpringDataAuditable
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByReportIdIn(List<Long> ids);

    @Query("select c from Comment c WHERE report_id in (:ids)")
    List<Comment> findAllUsingReportIds(@Param(value = "ids") List<Long> ids);

    @Query("select c from Comment c WHERE report_id = :reportId")
    List<Comment> findListUsingReportId(@Param(value = "reportId") long reportId);
}
