package FileExplorer.controller;

import FileExplorer.message.response.ResponseFile;
import FileExplorer.message.response.ResponseMessage;
import FileExplorer.model.File;
import FileExplorer.model.History;
import FileExplorer.repository.FileRepository;
import FileExplorer.repository.HistoryRepository;
import FileExplorer.security.services.FileService;
import FileExplorer.security.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.nio.file.Paths;
import  java.nio.file.Path;
import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    ServletContext context;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    HistoryService historyService;



    //Upload file to selected dir
    @PostMapping("/upload/{idDir}")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseMessage> uploadFileToDir(@RequestParam("file") MultipartFile file, @RequestParam("parrent") String idDir) {
        String message = "";
        try {
         fileService.storeToDir(file, idDir);

            //Date and time format to history register
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String stringDate = LocalDateTime.now().format(format);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            //save new history data
            historyRepository.save(new History(stringDate+" created new file "+ file.getOriginalFilename() +" to " + fileService.getPath(idDir)));
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


    //Download file
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        java.io.File file = fileService.getFile(id);
        File myFile = fileRepository.getOne(id);
        Path path = Paths.get(file.getAbsolutePath());
        try{
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            return  ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + myFile.getName() + "\"")
                    .body(resource.getByteArray());
        }catch (java.io.IOException e){
           String message = "Could not upload the file: " + file.getName() + "!";
           return   ResponseEntity.status(500).body(new byte[500]);
        }
    }

    //Create new dir
    @PostMapping("/dir/add")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<File> addDir(@RequestBody  File file) {


        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String stringDate = LocalDateTime.now().format(format);
        historyRepository.save(new History(stringDate+" created new dir "+ file.getName() +" to " + fileService.getPath(file.getId())));


        if( file.getParrentFile() != null) {
           // fileRepository.getOne(file.getParrentFile()).getChlids().add(file);
            file.setParent(fileRepository.getOne(file.getParrentFile()));
        }
        file.setType("folder");
        File newFile = fileService.addFile(file);

        return new ResponseEntity<>(newFile, HttpStatus.CREATED);
    }

    //Return list of dir
    @GetMapping("/allFiles")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    public List<File>  getAllDirs() {

        return fileService.getAllByParrent(null);

    }

    //delete dir or file
    @GetMapping("/dir/delete/{idFile}")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<File> deleteDir(@PathVariable String idFile) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String stringDate = LocalDateTime.now().format(format);
        historyRepository.save(new History(stringDate+" deleted "+ fileService.getOnebyId(idFile).getName() +" from " + fileService.getPath(idFile) + " and all content "));

        fileService.getFile(idFile).delete();
        fileService.removeById(idFile);


        return new ResponseEntity<>(HttpStatus.OK);

    }

    //Change dir or file location
    @PostMapping("/change")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ResponseMessage> changeParrent(@RequestParam("dir") String dir, @RequestParam("parent") String parent) {
        File parentFile;
        File file = fileService.getOnebyId(dir);
        if(fileService.getOnebyId(parent) != null)
            parentFile = fileService.getOnebyId(parent).getParent();
        else
            parentFile = fileService.getOnebyId(parent).getParent();

        file.setParent(parentFile);
        fileRepository.save(file);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String stringDate = LocalDateTime.now().format(format);
        historyRepository.save(new History(stringDate+" moved file"+ file.getName() +" from " + fileService.getPath(dir) + " to " + fileService.getPath(parent) ));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Get history register from database
    @GetMapping("/fullHistory")
    @PreAuthorize("hasRole('PM') or hasRole('ADMIN') or hasRole('USER')")
    public List<History>  getAllHistory() {

        return historyService.getAllHistory();

    }

}