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
import java.io.IOException;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Neal on 10/23 023.
 */
public class RejectFilter extends AccessControlFilter {
    Logger logger = LoggerFactory.getLogger(RejectFilter.class);

    public final static int NOT_LIMIT = -1;
    private final static String DEFAULT_REJECT_CACHE = "shiro-accountRejectCache";
    private final static String REJECT_KEY = "IS_REJECT";
    private final static String SUBJECT_NAME = "CURRENT_SUBJECT_NAME";

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
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }

        Session session = subject.getSession();
        String principal = (String) subject.getPrincipal();
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

        //judge current session is reject
        if (!deque.contains(sessionId) && session.getAttribute(REJECT_KEY) == null) {
            deque.push(sessionId);
            session.setAttribute(SUBJECT_NAME, principal);
        }

        while (deque.size() > maxSession) {
            Serializable rejectSessionId;
            if (rejectAfter) {
                rejectSessionId = deque.removeFirst();
            } else {
                rejectSessionId = deque.removeLast();
            }
            try {
                //Prevent cache info invalid , Use session
                Session rejectSession = sessionManager.getSession(new DefaultSessionKey(rejectSessionId));
                if (rejectSession != null) {
                    rejectSession.setAttribute(REJECT_KEY, true);
                }
            } catch (SessionException e) {
                logger.error(e.getMessage());
            }
        }

        if (session.getAttribute(REJECT_KEY) != null) {
            return rejectSubject(subject, request, response);
        }
        return true;
    }

    private boolean rejectSubject(Subject subject, ServletRequest request, ServletResponse response) throws IOException {
        subject.logout();
        saveRequest(request);
        WebUtils.issueRedirect(request, response, rejectUrl);
        return false;
    }

    public void clearCache(Session session) {
        Object subjectName = session.getAttribute(SUBJECT_NAME);
        if (subjectName != null) {
            Deque<Serializable> serializables = cache.get((String) subjectName);
            serializables.remove(session.getId());
        }
//
//        Deque<Serializable> deque = cache.get((String) subject.getPrincipal());
//        if (deque != null && deque.size() > 0) {
//            deque.remove(subject.getSession().getId());
//        }
    }

    public void setRejectUrl(String rejectUrl) {
        this.rejectUrl = rejectUrl;
    }

    public void setRejectAfter(boolean rejectAfter) {
        this.rejectAfter = rejectAfter;
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

    public void setRejectCache(String rejectCache) {
        this.rejectCache = rejectCache;
    }
}
