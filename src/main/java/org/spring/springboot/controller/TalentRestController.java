package org.spring.springboot.controller;

import org.spring.springboot.domain.Talent;
import org.spring.springboot.domain.TalentRequest;
import org.spring.springboot.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TalentRestController {

    @Autowired
    private TalentService talentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(){
        return "/index";
    }

    @RequestMapping(value = "/api/talent")
    @ResponseBody
    public String createTalent(@RequestBody Talent talent) {
        talent.setCreateTime(System.currentTimeMillis());
        return talentService.saveTalent(talent);
    }

    @RequestMapping(value = "/api/talent/search")
    @ResponseBody
    public List<Talent> searchTalent(TalentRequest talentRequest) {
        if(talentRequest.getPage() == null || talentRequest.getPage() < 0){
            talentRequest.setPage(0);
        }else{
            talentRequest.setPage(talentRequest.getPage() - 1);
        }
        if(talentRequest.getLimit() == null || talentRequest.getLimit() < 0){
            talentRequest.setLimit(10);
        }
        return talentService.searchTalent(talentRequest);
    }
}
