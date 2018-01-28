package com.talent.web;

import com.talent.domain.PageResult;
import com.talent.domain.QueryResult;
import com.talent.domain.Talent;
import com.talent.domain.TalentRequest;
import com.talent.service.TalentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class TalentRestController {

    @Autowired
    private TalentService talentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "/index";
    }

    @RequestMapping(value = "/api/talent/search")
    @ResponseBody
    public PageResult searchTalent(TalentRequest talentRequest) {
        // 空搜索不处理
        if (talentRequest.getQ() == null && "".equals(talentRequest.getQ())) {
            return new PageResult();
        }
        QueryResult<List<Talent>> queryResult = talentService.search(talentRequest);

        PageResult pageResult = new PageResult();
        pageResult.setRows(queryResult.getValue());
        pageResult.setTotal(queryResult.getTotal());
        return pageResult;
    }

    @RequestMapping(value = "/api/talent/download")
    public void download(HttpServletResponse response, TalentRequest talentRequest) {
        QueryResult<List<Talent>> queryResult = talentService.search(talentRequest);
        List<Talent> list = queryResult.getValue();
        if (list.size() > 0) {
            try {
                Talent talent = list.get(0);
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(talent.getTitle(), "UTF-8"));
                response.getOutputStream().write(talent.getDoc().getBytes("UTF-8"));
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
