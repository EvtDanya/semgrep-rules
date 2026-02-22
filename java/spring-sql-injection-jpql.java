// Уязвимый сервисный слой
@Service
public class UserSearchService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> searchUsers(String criteria, String sortBy) {
        // УЯЗВИМОСТЬ: Конкатенация строк в JPQL
        String jpql = "SELECT u FROM User u WHERE u.name LIKE '%" + criteria + "%' ORDER BY " + sortBy;

        Query query = entityManager.createQuery(jpql);
        return query.getResultList();
    }

    // УЯЗВИМОСТЬ: Небезопасное связывание параметров
    public User getUserByDynamicField(String fieldName, String value) {
        String jpql = "SELECT u FROM User u WHERE u." + fieldName + " = :value";
        return entityManager.createQuery(jpql, User.class)
            .setParameter("value", value)
            .getSingleResult();
    }
}