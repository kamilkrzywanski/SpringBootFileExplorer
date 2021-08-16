package FileExplorer.repository;

import FileExplorer.model.File;
import FileExplorer.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,Integer> {

    public List<History> findAllByOrderByIdAsc();


}
