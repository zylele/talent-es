package org.spring.springboot.service.impl;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.spring.springboot.domain.Talent;
import org.spring.springboot.domain.TalentRequest;
import org.spring.springboot.repository.TalentRepository;
import org.spring.springboot.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TalentESServiceImpl implements TalentService {

    @Autowired
    TalentRepository talentRepository;

    @Override
    public String saveTalent(Talent talent) {

        Talent talentResult = talentRepository.save(talent);
        return talentResult.getDoc();
    }

    @Override
    public List<Talent> searchTalent(TalentRequest talentRequest) {
        // 分页参数
        Pageable pageable = new PageRequest(talentRequest.getPage(), talentRequest.getLimit());

        // Function Score Query
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("doc", talentRequest.getQ())),
                    ScoreFunctionBuilders.weightFactorFunction(1000));

        // 创建搜索 DSL 查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();

        Page<Talent> searchPageResults = talentRepository.search(searchQuery);
        return searchPageResults.getContent();
    }

}
