package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.quaza.solutions.qpalx.elearning.domain.subscription.IPrepaidSubscriptionGenVO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
public class PrepaidSubscriptionGenVO implements IPrepaidSubscriptionGenVO {



    private Integer numToGenerate;

    private Long strategicPlatformPartnerID;

    private Long municipalityID;

    private Long subscriptionID;


    @Override
    public Integer getNumToGenerate() {
        return numToGenerate;
    }

    public void setNumToGenerate(Integer numToGenerate) {
        this.numToGenerate = numToGenerate;
    }

    @Override
    public Long getStrategicPlatformPartnerID() {
        return strategicPlatformPartnerID;
    }

    public void setStrategicPlatformPartnerID(Long strategicPlatformPartnerID) {
        this.strategicPlatformPartnerID = strategicPlatformPartnerID;
    }

    @Override
    public Long getMunicipalityID() {
        return municipalityID;
    }

    public void setMunicipalityID(Long municipalityID) {
        this.municipalityID = municipalityID;
    }

    @Override
    public Long getSubscriptionID() {
        return subscriptionID;
    }

    public void setSubscriptionID(Long subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("numToGenerate", numToGenerate)
                .append("strategicPlatformPartnerID", strategicPlatformPartnerID)
                .append("municipalityID", municipalityID)
                .append("subscriptionID", subscriptionID)
                .toString();
    }
    
}
