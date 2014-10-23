package com.nx.core.filters;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Neal on 10/23 023.
 */
public class RejectFilter extends AccessControlFilter {
    Logger logger = LoggerFactory.getLogger(RejectFilter.class);

    private final static int NOT_LIMIT = -1;
    private final static String DEFAULT_REJECT_CACHE = "shiro-accountRejectCache";

    /**
     * Reject url
     */
    private String rejectUrl;

    /**
     * Reject forward or backward , Default is forward
     */
    private boolean rejectAfter = false;

    /**
     * Max online same session number , Default is -1 , Not limit
     */
    private Integer maxSession = NOT_LIMIT;

    /**
     * Save reject information cache
     */
    private String rejectCache = DEFAULT_REJECT_CACHE;

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (maxSession == NOT_LIMIT) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }

        Session session = subject.getSession();
        String principal = subject.getPrincipal().toString();
        Serializable sessionId = session.getId();

        // Need thread synchronized ??
        Deque<Serializable> deque = cache.get(principal);
        if (deque == null) {
            synchronized (cache) {
                if (cache.get(principal) == null) {
                    deque = new LinkedList<>();
                    cache.put(principal, deque);
                }
            }
        }

        if (!deque.contains(sessionId) && session.getAttribute("reject") == null) {
            deque.push(sessionId);
        }

        if (session.getAttribute("isReject") != null) {
            subject.logout();
            saveRequest(request);
            WebUtils.issueRedirect(request, response, rejectUrl);
            return false;
        }


        while (deque.size() > maxSession) {
            Serializable rejectSessionId;
            if (rejectAfter) {
                rejectSessionId = deque.getFirst();
            } else {
                rejectSessionId = deque.getLast();
            }
            try {
                Session rejectSession = sessionManager.getSession(new DefaultSessionKey(rejectSessionId));
                if (rejectSession != null) {
                    rejectSession.setAttribute("isReject", true);
                }
                deque.remove(rejectSessionId);
            } catch (SessionException e) {
                logger.error(e.getMessage());
            }
        }


        return false;
    }


    public String getRejectUrl() {
        return rejectUrl;
    }

    public void setRejectUrl(String rejectUrl) {
        this.rejectUrl = rejectUrl;
    }

    public boolean isRejectAfter() {
        return rejectAfter;
    }

    public void setRejectAfter(boolean rejectAfter) {
        this.rejectAfter = rejectAfter;
    }

    public Integer getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(Integer maxSession) {
        this.maxSession = maxSession;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(rejectCache);
    }


    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public String getRejectCache() {
        return rejectCache;
    }

    public void setRejectCache(String rejectCache) {
        this.rejectCache = rejectCache;
    }
}
