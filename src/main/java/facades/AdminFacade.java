package facades;

import dtos.DinnerEventDTO;
import entities.Assignment;
import entities.DinnerEvent;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
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

    public DinnerEventDTO deleteEvent(String eventId){
        EntityManager em = emf.createEntityManager();

        int eventIdInt;
        try {
            eventIdInt = Integer.parseInt(eventId);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Contact IT...");
        }
        DinnerEvent dinnerEvent = em.find(DinnerEvent.class,eventIdInt);
        List<Assignment> assignments = dinnerEvent.getAssignments();

        //All assignments removed
        if (assignments != null){

            for (Assignment assignment: assignments) {
                em.getTransaction().begin();
                System.out.println(assignment.getFamilyName());

                em.remove(assignment);
                em.getTransaction().commit();
            }

        }

        DinnerEvent dinnerEventAfterAssignmentDelete = em.find(DinnerEvent.class,eventIdInt);

        em.getTransaction().begin();
        em.remove(dinnerEventAfterAssignmentDelete);
        em.getTransaction().commit();

        return new DinnerEventDTO(dinnerEvent);
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
