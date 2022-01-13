package facades;

import dtos.DinnerEventDTO;
import dtos.TransactionDTO;
import dtos.UserDTO;
import entities.Assignment;
import entities.DinnerEvent;
import entities.Transaction;
import entities.User;
import security.errorhandling.AuthenticationException;
import utils.MemberId;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
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

    //Us 2 account balance
    public double getAccountBalance(String userName){
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class,userName);
        } catch (WebApplicationException e){
            throw new WebApplicationException("Could not find user with user name: " + userName);
        }

        return user.getBalance();
    }
    public List<DinnerEventDTO> getAllEvents(){
        EntityManager em = emf.createEntityManager();
        List<DinnerEvent> dinnerEvents = em.createQuery("SELECT d from DinnerEvent d",DinnerEvent.class).getResultList();

        if (dinnerEvents == null){
            throw new WebApplicationException("No dinner events fond",404);
        }
        return DinnerEventDTO.getDtos(dinnerEvents);
    }

    public List<DinnerEventDTO> getAllEventsByUser(String userName) {
        EntityManager em = emf.createEntityManager();
User user = em.find(User.class,userName);
List<Assignment> assignments = user.getAssignments();
if (assignments == null){
    throw new WebApplicationException("You have no assignments yet. Add one!",404);
}
List<DinnerEvent> dinnerEvents = new ArrayList<>();
        for (Assignment assignment : assignments) {
            dinnerEvents.add(assignment.getDinnerEvent());
        }

return DinnerEventDTO.getDtos(dinnerEvents);
    }
//Første gang en bruger adder sig til et assignment kan der ikke tiløjes brugere.
   public UserDTO addMemberToEvent(UserDTO userDTO, String eventId){
       EntityManager em = emf.createEntityManager();
       int eventIdInt = Integer.parseInt(eventId);
       //Get event
       Query query = em.createQuery("SELECT e from DinnerEvent e where e.id = :eventIdInt",DinnerEvent.class);
      query.setParameter("eventIdInt",eventIdInt);
      DinnerEvent dinnerEvent = (DinnerEvent) query.getSingleResult();

      //get User
       Query query1 = em.createQuery("SELECT u from User u where u.userName = :userId",User.class);
       query1.setParameter("userId",userDTO.getUserName());
       User user = (User) query1.getSingleResult();

       //check if user already has an assignment for the dinner event.
       List<Assignment> assignments = user.getAssignments();
       for (Assignment assignment: assignments) {
           System.out.println(assignment.getDinnerEvent().equals(dinnerEvent));
           if(assignment.getDinnerEvent().equals(dinnerEvent)){
               throw new WebApplicationException("User already assigned to Dinner Event", 404);
           }

       }


       Assignment assignment= new Assignment(dinnerEvent,userDTO.getAssignment().getFamilyName(),userDTO.getAssignment().getContactInfo());
       Transaction transaction = new Transaction(assignment.getDinnerEvent().getPricePerPerson());


       user.addTransaction(transaction);
       user.addAssignment(assignment);

       em.getTransaction().begin();
       em.persist(user);
       em.getTransaction().commit();
      //dinnerEvent.ad



        return new UserDTO(user);
   }



    public List<UserDTO> addMembersToEvent(List<MemberId> memberIds, String eventId){
        EntityManager em = emf.createEntityManager();
        List<UserDTO> userDTOS = new ArrayList<>();

        for (MemberId memberId : memberIds) {
            Query query2 = em.createQuery("SELECT u from User u where u.userName = :userId",User.class);
            query2.setParameter("userId",memberId.getId());
           User user = (User) query2.getSingleResult();

            Query query3 = em.createQuery("SELECT a from Assignment a where a.id = :id",Assignment.class);
            query3.setParameter("id",memberId.getAssignmentId());
            Assignment assignment = (Assignment) query3.getSingleResult();
            assignment.addUser(user);

            Transaction transaction = new Transaction(assignment.getDinnerEvent().getPricePerPerson());
            user.addTransaction(transaction);

            //TODO: Måske fejl
            for (Assignment assignment1: user.getAssignments()) {
                if (assignment1.getUsers().contains(user)){
                    throw new WebApplicationException(user.getUserName() + " is already assigned to the Dinner Event", 404);
                }
            }

            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();

            userDTOS.add(new UserDTO(user));

        }



        return userDTOS;
    }

    //US-5
    /*
    public List<DinnerEventDTO> getAllEventsByUser(String userName) {
        EntityManager em = emf.createEntityManager();
User user = em.find(User.class,userName);
List<Assignment> assignments = user.getAssignments();
if (assignments == null){
    throw new WebApplicationException("You have no assignments yet. Add one!",404);
}
List<DinnerEvent> dinnerEvents = new ArrayList<>();
        for (Assignment assignment : assignments) {
            dinnerEvents.add(assignment.getDinnerEvent());
        }

return DinnerEventDTO.getDtos(dinnerEvents);
    }
     */
    public List<UserDTO> getAllMembersAssignedToEvent(String eventId) {
        EntityManager em = emf.createEntityManager();
        Integer eventIdInt = Integer.parseInt(eventId);
        List<Assignment> assignments = em.find(DinnerEvent.class,eventIdInt).getAssignments();
       // List<Assignment> assignments = dinnerEvent.getAssignments();
        //Query query = em.createQuery("SELECT ")
        List<User> users1 = em.createQuery("select u from User u").getResultList();
        List<Assignment> assignments1 = new ArrayList<>();
        for (User user: users1) {
            for (Assignment assignment :user.getAssignments()) {
                assignments1.add(assignment);
            }


        }
        System.out.println(assignments1);

        if (assignments == null){
            throw new WebApplicationException("No assignments yet");
        }
        List<User> users = new ArrayList<>();
        for (Assignment assignment : assignments1) {
            for (User user : assignment.getUsers()) {
                users.add(user);
            }
        }


        return UserDTO.getDtos(users);
    }

    public List<TransactionDTO> getAllTransactionsById(String userName){
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class,userName);
        List<Transaction> transactions = user.getTransactions();


        return TransactionDTO.getDtos(transactions);
    }


}
