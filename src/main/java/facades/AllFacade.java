package facades;

import dtos.DinnerEventDTO;
import entities.DinnerEvent;
import entities.User;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class AllFacade {

    private static EntityManagerFactory emf;
    private static AllFacade instance;

    private AllFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static AllFacade getAllFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AllFacade();
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

}
