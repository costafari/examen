package sv.com.prueba.controller;

import jakarta.annotation.ManagedBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.com.prueba.dao.ReadersDAO;
import sv.com.prueba.entity.Readers;

import java.io.Serializable;
import java.util.List;

@Named("readersView")
@ViewScoped
public class ReaderController implements Serializable {

    private List<Readers> readersItems;
    @Inject
    private ReadersDAO readersDAO;
    @PostConstruct
    public void getAllReaders() {
        readersItems = readersDAO.findAllReaders();
    }

    public List<Readers> getReadersItems() {
        return readersItems;
    }

    public void setReadersItems(List<Readers> readersItems) {
        this.readersItems = readersItems;
    }
}
