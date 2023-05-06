package com.example.userserver.service;

import com.example.userserver.entity.DbSequence;
import com.example.userserver.repository.UserRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class SequenceGeneratorService {


    private MongoOperations mongoOperations;

    private final UserRepository userRepository;

    public SequenceGeneratorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public int getSequenceNumber(String sequenceName) {
        //get sequence no
        Query query = new Query(Criteria.where("id").is(sequenceName));
        //update the sequence no
        Update update = new Update().inc("seq", 1);
        //modify in document
        DbSequence counter = mongoOperations.findAndModify(query,
                        update, options().returnNew(true).upsert(true),
                        DbSequence.class);


        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public int countId (){
        int count = (int)userRepository.findAll().stream().count();
        return count+1;
    }
}
