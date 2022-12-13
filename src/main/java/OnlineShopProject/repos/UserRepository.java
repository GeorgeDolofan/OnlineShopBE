package OnlineShopProject.repos;

import OnlineShopProject.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.List;

public interface UserRepository extends MongoRepository<User, String>{
    @Query
    List<User> findByusername(String username);
    List<User> findByemailAddress(String emailAddress);
}
