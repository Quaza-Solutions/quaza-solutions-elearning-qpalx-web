package com.quaza.solutions.qpalx.elearning.web.security.login;

import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXMunicipality;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.ContentAdminProfileHolder;
import com.quaza.solutions.qpalx.elearning.domain.subscription.SubscriptionStatusE;
import com.quaza.solutions.qpalx.elearning.domain.subscription.SubscriptionValidationResult;
import com.quaza.solutions.qpalx.elearning.service.geographical.IGeographicalDateTimeFormatter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

/**
 * Web only QPalXUser object which hides the underlying Domain data Object model for QPalXUser and exposes only relevant Web only
 * methods for clients.
 *
 * @author manyce400
 */
public class WebQPalXUser implements UserDetails {




    private Optional<QPalXUser> qPalXUser = Optional.empty();

    private final SubscriptionValidationResult subscriptionValidationResult;

    private Optional<ContentAdminProfileHolder> contentAdminProfileHolder = Optional.empty();

    private IGeographicalDateTimeFormatter iGeographicalDateTimeFormatter;


    public WebQPalXUser() {
        this(new SubscriptionValidationResult(SubscriptionStatusE.INVALID), null);
    }

    public WebQPalXUser(SubscriptionValidationResult subscriptionValidationResult) {
        this(subscriptionValidationResult, null);
    }

    public WebQPalXUser(SubscriptionValidationResult subscriptionValidationResult, IGeographicalDateTimeFormatter iGeographicalDateTimeFormatter) {
        this.subscriptionValidationResult = subscriptionValidationResult;
        this.iGeographicalDateTimeFormatter = iGeographicalDateTimeFormatter;
    }

    public WebQPalXUser(final QPalXUser qPalXUser, SubscriptionValidationResult subscriptionValidationResult) {
        this.qPalXUser = Optional.of(qPalXUser);
        this.subscriptionValidationResult = subscriptionValidationResult;
    }

    public WebQPalXUser(final QPalXUser qPalXUser, ContentAdminProfileHolder contentAdminProfileHolder) {
        this.qPalXUser = Optional.of(qPalXUser);
        this.contentAdminProfileHolder = Optional.of(contentAdminProfileHolder);
        this.subscriptionValidationResult = null;
    }

    public String getUserFullName() {
        if (qPalXUser.isPresent()) {
            return new StringBuffer(qPalXUser.get().getFirstName())
                    .append(" ")
                    .append(qPalXUser.get().getLastName())
                    .toString();
        }

        return null;
    }

    public String getEmail() {
        if (qPalXUser.isPresent()) {
            return qPalXUser.get().getEmail();
        }

        return null;
    }

    public String getSuccessID() {
        if (qPalXUser.isPresent()) {
            return qPalXUser.get().getSuccessID();
        }

        return null;
    }


    public String getUserSubscriptionExpiryDate() {
        if (qPalXUser.isPresent()) {
            DateTime expiryDate = subscriptionValidationResult.getExpirationDate();
            QPalXMunicipality studentMunicipality = qPalXUser.get().getQPalXMunicipality();
            return iGeographicalDateTimeFormatter.getDisplayDateTimeWithTimeZone(expiryDate, studentMunicipality);
        }

        return null;
    }

    public QPalXUser getQPalXUser() {
        return qPalXUser.get();
    }

    public Optional<ContentAdminProfileHolder> getContentAdminProfileHolder() {
        return contentAdminProfileHolder;
    }

    public boolean hasValidQPalXUser() {
        return qPalXUser.isPresent();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (qPalXUser.isPresent()) {
            return AuthorityUtils.createAuthorityList(qPalXUser.get().getUserType().toString());
        }

        return AuthorityUtils.createAuthorityList();
    }

    @Override
    public String getPassword() {
        if (qPalXUser.isPresent()) {
            return qPalXUser.get().getPassword();
        }

        return null;
    }

    @Override
    public String getUsername() {
        if (qPalXUser.isPresent()) {
            return qPalXUser.get().getEmail();
        }

        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (qPalXUser.isPresent()) {
            return !qPalXUser.get().isAccountLocked(); // Return inverse since isAccountLocked will return false if not locked
        }

        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // By default return true since this functionality is not yet supported in QPalX
    }

    @Override
    public boolean isEnabled() {
        if (qPalXUser.isPresent()) {
            return !qPalXUser.get().isAccountLocked(); // Return inverse since isAccountLocked will return false if not locked
        }

        return false;
    }

    public SubscriptionValidationResult getSubscriptionValidationResult() {
        return subscriptionValidationResult;
    }

    public void setIGeographicalDateTimeFormatter(IGeographicalDateTimeFormatter iGeographicalDateTimeFormatter) {
        this.iGeographicalDateTimeFormatter = iGeographicalDateTimeFormatter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("qPalXUser", qPalXUser)
                .append("subscriptionValidationResult", subscriptionValidationResult)
                .append("contentAdminProfileHolder", contentAdminProfileHolder)
                .append("iGeographicalDateTimeFormatter", iGeographicalDateTimeFormatter)
                .toString();
    }


    public static WebQPalXUser instance(QPalXUser qPalXUser, ContentAdminProfileHolder contentAdminProfileHolder) {
        return new WebQPalXUser(qPalXUser, contentAdminProfileHolder);
    }
}
