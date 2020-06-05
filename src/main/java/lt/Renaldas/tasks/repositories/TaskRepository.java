package lt.Renaldas.tasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.Renaldas.tasks.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByName(String name);

}
