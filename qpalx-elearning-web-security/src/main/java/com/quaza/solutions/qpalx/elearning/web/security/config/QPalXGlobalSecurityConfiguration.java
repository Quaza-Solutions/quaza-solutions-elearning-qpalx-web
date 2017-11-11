package com.quaza.solutions.qpalx.elearning.web.security.config;

import com.google.common.collect.Lists;
import com.quaza.solutions.qpalx.elearning.web.security.login.DefaultQPalXAuthenticationSuccessFailureHandler;
import com.quaza.solutions.qpalx.elearning.web.security.login.DefaultQPalXLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

/**
 * Configures Spring-Boot Security functionality as part of QPalX Web application.  This enforces global
 * application wide login security.
 *
 * Users attempt to access any other pages except the ones specified in the below configuration will be
 * required to login before proceeding.
 *
 * Note that handlers are explicitly used here to determine where a user gets redirected to on succesful or failed login.
 *
 * @author manyce400
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class QPalXGlobalSecurityConfiguration extends WebSecurityConfigurerAdapter {



    // Autowire in the user details service to load user by id from database
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DefaultQPalXLogoutHandler defaultQPalXLogoutHandler;

    @Autowired
    private DefaultQPalXAuthenticationSuccessFailureHandler defaultQPalXAuthenticationSuccessFailureHandler;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QPalXGlobalSecurityConfiguration.class);


    /**
     * Note that we have to exclusively permit access to all css, js and img directory content  as Spring Security will block IF not.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("Configuring global QPalX Security settings with spring-boot-starter-security, logins will be enforced...");
        List<String> validationPathsexclustionList = getPathsToExcludeFromValidation();
        http.authorizeRequests()
                .antMatchers(validationPathsexclustionList.toArray(new String[0]))
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(defaultQPalXAuthenticationSuccessFailureHandler) // Using a customized handler for successful login authentication redirection
                .failureHandler(defaultQPalXAuthenticationSuccessFailureHandler) // Using a customized handler for unsuccessful login authentication redirection
                .and()
                .logout()
                .addLogoutHandler(defaultQPalXLogoutHandler) // Using a customized handler for logouts
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // we register this URL as the URL to trigger a Spring Security logout
                .logoutSuccessUrl("/") // redirect after logout is successful
                .invalidateHttpSession(true)
                .permitAll();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.info("Configuring spring-boot-starter-security with UserDetailsService implementation: {}", userDetailsService);
        auth.userDetailsService(userDetailsService);
    }

    // Returns a List of paths for request mappings that will be excluded from QPalX Global validation.  Access to these will not require login.
    private List<String> getPathsToExcludeFromValidation() {
        List<String> exclustionList = Lists.newArrayList(
                "/", "/css/**", "/css/**/**", "/js/**", "/img/**", "/images/**", "/font/**", "/videos/**", "/QGatewaySignup", "/QPalxSubscribptionEducatioProfile",
                "/QPalxSubscribptionPayment", "/QPalxSubscribptionConfirm", "/QPalxSubscribptionMobilePaySetup", "/QPalxSubscribptionCancel", "/QPalxSubscribptionProcess",
                "/QPalxSubscribptionComplete", "/QPalXGateway", "/qpalx-access-failure", "/ConfirmSubscriptionRenewal", "/GetSubscriptionRenewalToken", "/SubscribptionRenewalComplete", "/renew-subscription-with-payment",
                "/PerformanceSnapshot", "/qpalx-sign-up", "/sign-up-type-select", "/select-signup-payment", "/customize-proficiency-ranking", "/complete-qpalx-signup", "/generateIds",
                "/micro-lesson-progress-tracker", "/question-bank-progress-tracker", "/track-microlesson-quiz", "/StudentQuizPerformance", "/FindEducationalInstitutionsMatching", "/find-academic-levels-by-municipality",
                "/find-student-tutorial-grades-by-academic-level", "/link-guardian-student", "/complete-guardian-registration"
        );

        LOGGER.info("Paths to exclude from Global Security Authentication:> {}", exclustionList);
        return exclustionList;
    }
}

