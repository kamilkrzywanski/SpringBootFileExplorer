package FileExplorer.component;


import FileExplorer.component.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;

@Component
public class StartupComponent implements CommandLineRunner {
    private final StorageProperties storageProps;

    @Autowired
    ServletContext context;

    @Autowired
    StorageProperties storageProperties;


    public StartupComponent (StorageProperties storageProps){
        this.storageProps = storageProps;
    }


    //Create dir to storage files while first run
    @Override
    public void run(String... args)  throws Exception{
        String path = storageProperties.getPath();

        File file = new File(path);

        if(!file.exists())
            if(!file.mkdir())
                throw new Exception("Cannot to create new dir");


    }


}