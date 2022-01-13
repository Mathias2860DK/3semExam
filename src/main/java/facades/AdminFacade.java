package facades;

import dtos.DinnerEventDTO;
import entities.DinnerEvent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;
import java.util.Date;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class AdminFacade {

    private static EntityManagerFactory emf;
    private static AdminFacade instance;

    private AdminFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static AdminFacade getAdminFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdminFacade();
        }
        return instance;
    }

    public List<DinnerEventDTO> getAllEvents(){
        EntityManager em = emf.createEntityManager();
        List<DinnerEvent> dinnerEvents = em.createQuery("SELECT d from DinnerEvent d",DinnerEvent.class).getResultList();

        if (dinnerEvents == null){
            throw new WebApplicationException("No dinner events fond",404);
        }
        return DinnerEventDTO.getDtos(dinnerEvents);
    }

    public DinnerEventDTO addDinnerEvent(DinnerEventDTO dinnerEventDTO){
        EntityManager em = emf.createEntityManager();
        dinnerEventDTO.setTime(new Date());//TODO: custom time
        DinnerEvent dinnerEvent = new DinnerEvent(dinnerEventDTO.getLocation(), dinnerEventDTO.getDish(), dinnerEventDTO.getPricePerPerson(),
                dinnerEventDTO.getTime());
        if ((dinnerEvent.getDish().length() == 0) || (dinnerEvent.getLocation().length() == 0)) {
            throw new WebApplicationException("Info is missing", 400);
        }
        em.getTransaction().begin();
        em.persist(dinnerEvent);
        em.getTransaction().commit();

    return new DinnerEventDTO(dinnerEvent);
    }

}
