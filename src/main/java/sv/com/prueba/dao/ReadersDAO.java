package sv.com.prueba.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import sv.com.prueba.entity.Blogs;
import sv.com.prueba.entity.Readers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.UserTransaction;
import sv.com.prueba.exception.NonexistentEntityException;
import sv.com.prueba.exception.PreexistingEntityException;
import sv.com.prueba.exception.RollbackFailureException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
@Transactional
public class ReadersDAO implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public List<Readers> findAllReaders() {
        return em.createQuery("SELECT r FROM Readers r", Readers.class).getResultList();
    }

    private EntityManagerFactory emf = null;
    private UserTransaction utx = null;
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ReadersDAO() {
    }

    public ReadersDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }

    public void create(Readers readers) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (readers.getBlogsList() == null) {
            readers.setBlogsList(new ArrayList<Blogs>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Blogs> attachedBlogsList = new ArrayList<Blogs>();
            for (Blogs blogsListBlogsToAttach : readers.getBlogsList()) {
                blogsListBlogsToAttach = em.getReference(blogsListBlogsToAttach.getClass(), blogsListBlogsToAttach.getId());
                attachedBlogsList.add(blogsListBlogsToAttach);
            }
            readers.setBlogsList(attachedBlogsList);
            em.persist(readers);
            for (Blogs blogsListBlogs : readers.getBlogsList()) {
                blogsListBlogs.getReadersList().add(readers);
                blogsListBlogs = em.merge(blogsListBlogs);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findReaders(readers.getId()) != null) {
                throw new PreexistingEntityException("Readers " + readers + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Readers findReaders(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Readers.class, id);
        } finally {
            em.close();
        }
    }

    public void edit(Readers readers) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Readers persistentReaders = em.find(Readers.class, readers.getId());
            List<Blogs> blogsListOld = persistentReaders.getBlogsList();
            List<Blogs> blogsListNew = readers.getBlogsList();
            List<Blogs> attachedBlogsListNew = new ArrayList<Blogs>();
            for (Blogs blogsListNewBlogsToAttach : blogsListNew) {
                blogsListNewBlogsToAttach = em.getReference(blogsListNewBlogsToAttach.getClass(), blogsListNewBlogsToAttach.getId());
                attachedBlogsListNew.add(blogsListNewBlogsToAttach);
            }
            blogsListNew = attachedBlogsListNew;
            readers.setBlogsList(blogsListNew);
            readers = em.merge(readers);
            for (Blogs blogsListOldBlogs : blogsListOld) {
                if (!blogsListNew.contains(blogsListOldBlogs)) {
                    blogsListOldBlogs.getReadersList().remove(readers);
                    blogsListOldBlogs = em.merge(blogsListOldBlogs);
                }
            }
            for (Blogs blogsListNewBlogs : blogsListNew) {
                if (!blogsListOld.contains(blogsListNewBlogs)) {
                    blogsListNewBlogs.getReadersList().add(readers);
                    blogsListNewBlogs = em.merge(blogsListNewBlogs);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = readers.getId();
                if (findReaders(id) == null) {
                    throw new NonexistentEntityException("The readers with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Readers> findReadersEntities() {
        return findReadersEntities(true, -1, -1);
    }

    private List<Readers> findReadersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Readers.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}

