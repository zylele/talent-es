
package org.spring.springboot.service;

import org.spring.springboot.domain.Talent;
import org.spring.springboot.domain.TalentRequest;

import java.util.List;

public interface TalentService {

    String saveTalent(Talent talent);

    List<Talent> searchTalent(TalentRequest talentRequest);
}