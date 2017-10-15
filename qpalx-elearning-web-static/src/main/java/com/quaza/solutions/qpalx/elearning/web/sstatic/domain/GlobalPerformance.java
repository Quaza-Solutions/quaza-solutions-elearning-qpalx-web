package com.quaza.solutions.qpalx.elearning.web.sstatic.domain;

import com.quaza.solutions.qpalx.elearning.domain.subjectmatter.proficiency.ProficiencyRankingScaleE;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author manyce400
 */
public class GlobalPerformance {



    private final String qpalxExperienceLevel;


    private final Set<ProficiencyRankingScaleE> acheivedProficiencies = new TreeSet<>(new ProficiencyRankingScaleE.ProficiencyComparator());

    public static final String CLASS_ATTRIBUTE = "GlobalPerformance";


    public GlobalPerformance(String qpalxExperienceLevel, ProficiencyRankingScaleE currentProficiencyRankingScaleE) {
        Assert.notNull(qpalxExperienceLevel, "qpalxExperienceLevel cannot be null");
        Assert.notNull(currentProficiencyRankingScaleE, "currentProficiencyRankingScaleE cannot be null");
        this.qpalxExperienceLevel = qpalxExperienceLevel;
        buildProficiencyRankingScaleE(currentProficiencyRankingScaleE);
    }

    public String getQpalXExperienceLevel() {
        return qpalxExperienceLevel;
    }

    public Set<ProficiencyRankingScaleE> getAcheivedProficiencies() {
        return acheivedProficiencies;
    }

    private void buildProficiencyRankingScaleE(ProficiencyRankingScaleE currentProficiencyRankingScaleE) {
        acheivedProficiencies.add(currentProficiencyRankingScaleE);
        while(ProficiencyRankingScaleE.getProficiencyRankingScaleEBelow(currentProficiencyRankingScaleE).isPresent()) {
            ProficiencyRankingScaleE proficiencyRankingScaleE = ProficiencyRankingScaleE.getProficiencyRankingScaleEBelow(currentProficiencyRankingScaleE).get();
            acheivedProficiencies.add(proficiencyRankingScaleE);
        }
    }

    public static final GlobalPerformance newInstance(String qpalxExperienceLevel, ProficiencyRankingScaleE currentProficiencyRankingScaleE) {
        return new GlobalPerformance(qpalxExperienceLevel, currentProficiencyRankingScaleE);
    }
}
