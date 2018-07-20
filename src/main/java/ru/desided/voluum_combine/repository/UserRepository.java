package ru.desided.voluum_combine.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.desided.voluum_combine.entity.User;

import java.util.List;


//string check PK
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByNick(String login);
    List<User> findAllByNick(String nick);
    User findByVoluumLoginAndVoluumPassword(String login, String password);

//    List<User> findByUserId(Integer integer);
}
