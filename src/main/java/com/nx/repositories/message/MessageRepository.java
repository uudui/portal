package com.nx.repositories.message;

import com.nx.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Neal on 2014-09-28.
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
    @Transactional
    <T extends Message> T save(T entry);
}
