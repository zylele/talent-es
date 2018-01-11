package org.spring.springboot.repository;

import org.spring.springboot.domain.Talent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalentRepository extends ElasticsearchRepository<Talent,Long> {


}
