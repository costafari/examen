package sv.com.prueba.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "readers")
public class Readers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "readersList")
    private List<Blogs> blogsList;

    public Readers() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blogs> getBlogsList() {
        return blogsList;
    }

    public void setBlogsList(List<Blogs> blogsList) {
        this.blogsList = blogsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Readers)) return false;
        Readers readers = (Readers) o;
        return Objects.equals(id, readers.id) && Objects.equals(name, readers.name) && Objects.equals(blogsList, readers.blogsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, blogsList);
    }

    @Override
    public String toString() {
        return "Readers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blogsList=" + blogsList +
                '}';
    }

}
