package sv.com.prueba.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "blogs")
public class Blogs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @JoinTable(name = "blogs_readers", joinColumns = {
            @JoinColumn(name = "b_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "r_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Readers> readersList;

    public Blogs() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Readers> getReadersList() {
        return readersList;
    }

    public void setReadersList(List<Readers> readersList) {
        this.readersList = readersList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blogs)) return false;
        Blogs blogs = (Blogs) o;
        return Objects.equals(id, blogs.id) && Objects.equals(title, blogs.title) && Objects.equals(description, blogs.description) && Objects.equals(readersList, blogs.readersList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, readersList);
    }

    @Override
    public String toString() {
        return "Blogs{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", readersList=" + readersList +
                '}';
    }
}
