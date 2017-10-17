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

    private final ProficiencyRankingScaleE currentProficiencyRankingScaleE;


    private final Set<ProficiencyRankingScaleE> acheivedProficiencies = new TreeSet<>(new ProficiencyRankingScaleE.ProficiencyComparator());

    public static final String CLASS_ATTRIBUTE = "GlobalPerformance";


    public GlobalPerformance(String qpalxExperienceLevel, ProficiencyRankingScaleE currentProficiencyRankingScaleE) {
        Assert.notNull(qpalxExperienceLevel, "qpalxExperienceLevel cannot be null");
        Assert.notNull(currentProficiencyRankingScaleE, "currentProficiencyRankingScaleE cannot be null");
        this.qpalxExperienceLevel = qpalxExperienceLevel;
        this.currentProficiencyRankingScaleE = currentProficiencyRankingScaleE;
        buildProficiencyRankingScaleE(currentProficiencyRankingScaleE);
    }

    public String getQpalXExperienceLevel() {
        return qpalxExperienceLevel;
    }

    public ProficiencyRankingScaleE getCurrentProficiencyRankingScaleE() {
        return currentProficiencyRankingScaleE;
    }

    public Set<ProficiencyRankingScaleE> getAcheivedProficiencies() {
        return acheivedProficiencies;
    }

    private void buildProficiencyRankingScaleE(ProficiencyRankingScaleE currentProficiencyRankingScaleE) {
        System.out.println("currentProficiencyRankingScaleE = " + currentProficiencyRankingScaleE);
        Set<ProficiencyRankingScaleE> results = ProficiencyRankingScaleE.getAllProficiencyRankingsInScope(ProficiencyRankingScaleE.ONE.getProficiencyRanking(), currentProficiencyRankingScaleE.getProficiencyRanking());
        System.out.println("results = " + results);
        acheivedProficiencies.addAll(results);
    }

    public static final GlobalPerformance newInstance(String qpalxExperienceLevel, ProficiencyRankingScaleE currentProficiencyRankingScaleE) {
        return new GlobalPerformance(qpalxExperienceLevel, currentProficiencyRankingScaleE);
    }
}
