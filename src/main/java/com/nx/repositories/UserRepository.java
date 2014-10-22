package com.nx.repositories;

import com.nx.domain.security.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Neal on 10/12 012.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    @Query
    public User findByName(String name);
}
