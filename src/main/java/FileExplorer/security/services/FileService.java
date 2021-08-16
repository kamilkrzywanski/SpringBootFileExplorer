package FileExplorer.security.services;

import FileExplorer.component.StorageProperties;
import FileExplorer.model.File;
import FileExplorer.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.sound.midi.Patch;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Stream;


@Service
@Transactional
public class FileService {
    @Autowired
    FileRepository fileRepository;

    @Autowired
    ServletContext context;

    @Autowired
    StorageProperties storageProperties;


    public void storeToDir(MultipartFile file, String idDir) throws IOException {
       // String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String path = storageProperties.getPath();

        File fileToSave = new File(file.getOriginalFilename(), file.getContentType(), fileRepository.getOne(idDir));
        String id = fileRepository.save(fileToSave).getId();

        java.io.File fileDest = new java.io.File(path, id);
        file.transferTo(fileDest);
    }



    public Stream<File> getAllFiles() {
        return fileRepository.findAll().stream();
    }


    public List<File> getAllFile(){return fileRepository.findAll();}


    public File addFile(File file){

        return fileRepository.save(file);
    }

    public File getOnebyId(String id) {
        return fileRepository.getOne(id);
    }


    public void removeById(String id) {
        fileRepository.deleteById(id);
    }


    public List<File> getAllByParrent(String parent) {
        return fileRepository.getAllFileByparent(parent);
    }

    public java.io.File getFile(String id) {
        String path = storageProperties.getPath();
        return new java.io.File(path,id);
    }

    public String getPath(String id) {

        String path = new String();
        //File file = fileRepository.getOne(id);
        File tmp;

        if(id!= null) {
            for (File file = fileRepository.getOne(id); file != null; ) {
                path = "\\" + file.getName() + "\\" + path;
                file = file.getParent();
            }
        }

        path = storageProperties.getPath() + path;
        return path;
    }


}
