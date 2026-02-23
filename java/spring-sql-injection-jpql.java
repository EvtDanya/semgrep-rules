@RestController
public class UserController {

    private final UserSearchService service;

    // Source: @RequestParam → service → createQuery
    @GetMapping("/search")
    public List<User> search(@RequestParam String criteria,
                             @RequestParam String sortBy) {
        return service.searchUsers(criteria, sortBy);
    }

    @GetMapping("/user")
    public User getByField(@RequestParam String fieldName,
                           @RequestParam String value) {
        return service.getUserByDynamicField(fieldName, value);
    }
}

@Service
public class UserSearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> searchUsers(String criteria, String sortBy) {
        String jpql = "SELECT u FROM User u WHERE u.name LIKE '%"
                + criteria + "%' ORDER BY " + sortBy;
        // ruleid: spring-sql-injection-jpql
        return entityManager.createQuery(jpql).getResultList();
    }

    public User getUserByDynamicField(String fieldName, String value) {
        String jpql = "SELECT u FROM User u WHERE u." + fieldName + " = :value";
        // ruleid: spring-sql-injection-jpql
        return entityManager.createQuery(jpql, User.class)
                .setParameter("value", value)
                .getSingleResult();
    }

    // Safe: parameterized query, no concatenation of user input
    public List<User> searchSafe(String criteria) {
        String jpql = "SELECT u FROM User u WHERE u.name LIKE :criteria";
        // ok: spring-sql-injection-jpql
        return entityManager.createQuery(jpql)
                .setParameter("criteria", "%" + criteria + "%")
                .getResultList();
    }
}