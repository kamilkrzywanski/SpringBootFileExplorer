package FileExplorer.repository;

import FileExplorer.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File,String> {

    List<File> getAllFileByparrentFile(String parrentFile);

    List<File> getAllFileByparent(File file);

    List<File> getAllFileByparent(String id);


}
