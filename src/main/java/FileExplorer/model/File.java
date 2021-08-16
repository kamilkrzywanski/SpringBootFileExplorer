package FileExplorer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "file")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class File{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String parrentFile;

    @NotBlank
    private String name;

    @NotBlank
    private String type;


    private String data;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    @JsonBackReference
    private File parent;

    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY,  cascade = CascadeType.ALL, orphanRemoval = true )
    private List<File> children;




    public File() {
    }

    public File(String name, String type) {
        this.name = name;
        this.type = type;

    }

    public File(String name, String type, File parent) {
        this.name = name;
        this.type = type;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public File getParent() {
        return parent;
    }

    public List<File> getChlids() {
        return children;
    }

    public void setParent(File parent) {
        this.parent = parent;
    }

    public void setChildren(List<File> children) {
        this.children = children;
    }



    public String getParrentFile() {
        return parrentFile;
    }

    public List<File> getChildren() {
        return children;
    }

    public void setParrentFile(String parrentFile) {
        this.parrentFile = parrentFile;
    }
}
