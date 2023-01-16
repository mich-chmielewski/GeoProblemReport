package pl.mgis.problemreport.repository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mgis.problemreport.model.Setting;

import java.util.List;
import java.util.Optional;

@JaversSpringDataAuditable
@Repository
public interface SettingRepository extends JpaRepository<Setting,Long> {

    Optional<Setting> findByKey(String key);

}
