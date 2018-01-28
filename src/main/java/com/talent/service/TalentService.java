package com.talent.service;

import com.alibaba.fastjson.JSON;
import com.talent.config.ElasticSearchConfig;
import com.talent.domain.QueryResult;
import com.talent.domain.Talent;
import com.talent.domain.TalentRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TalentService {

    private static final Logger LOG = LogManager.getLogger(ElasticSearchConfig.class);

    @Autowired
    private TransportClient client;

    @Value("${spring.elasticsearch.talent.index}")
    private String index;

    @Value("${spring.elasticsearch.talent.type}")
    private String type;

    public void generateIndex(Talent talent) {
        IndexResponse response = client.prepareIndex(index, type)
                .setSource(JSON.toJSONString(talent).getBytes(), XContentType.JSON)
                .get();
        LOG.info("response=" + response);
    }

    public QueryResult<List<Talent>> search(TalentRequest request) {

        int start = (request.getPage() - 1) * request.getLimit();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(start)
                .setSize(request.getLimit());

        QueryBuilder queryBuilder;
        if (request.getQ() == null || "".equals(request.getQ())) {
            queryBuilder = QueryBuilders.matchAllQuery();
        } else {
            queryBuilder = QueryBuilders.matchQuery("doc", request.getQ());
        }
        if(request.getId() != null){
            queryBuilder = QueryBuilders.termQuery("_id", request.getId());
        }

        searchRequestBuilder.setQuery(queryBuilder);

        HighlightBuilder highlightBuilder = new HighlightBuilder().field("doc").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        searchRequestBuilder.highlighter(highlightBuilder);

        SearchResponse response = searchRequestBuilder.get();

        List<Talent> list = new ArrayList<>();
        for (SearchHit hit : response.getHits()) {
            String source = hit.getSourceAsString();

            Talent talent = JSON.parseObject(source, Talent.class);
            talent.setId(hit.getId());

            HighlightField nameField = hit.getHighlightFields().get("doc");
            if (nameField != null) {
                //处理高亮片段
                Text[] fragments = nameField.fragments();
                StringBuilder nameTmp = new StringBuilder();
                for (Text text : fragments) {
                    nameTmp.append(text).append("...");
                }
                //将高亮片段组装到结果中去
                talent.setDoc(nameTmp.toString());
            }

            list.add(talent);
        }

        QueryResult<List<Talent>> queryResult = new QueryResult<>();
        queryResult.setTotal(response.getHits().getTotalHits());
        queryResult.setValue(list);

        return queryResult;
    }

}
