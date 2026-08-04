package com.devaihub.backend.repository;
import com.devaihub.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devaihub.backend.entity.User;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProjectRepository extends JpaRepository<Project,Long>{
    List<Project> findByOwner(User owner);

    Optional<Project> findById(Long id);

}
