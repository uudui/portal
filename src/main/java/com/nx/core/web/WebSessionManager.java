package com.nx.core.web;

import com.nx.core.filters.RejectFilter;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Neal on 10/15 015.
 */
public class WebSessionManager extends DefaultWebSessionManager {
    private final static Logger logger = LoggerFactory.getLogger(WebSessionManager.class);

    public static AtomicInteger ONLINE = new AtomicInteger(0);

    private RejectFilter rejectFilter;

    @Override
    protected void onStart(Session session, SessionContext context) {
        super.onStart(session, context);
        logger.info("Session Create, Online#{}", ONLINE.incrementAndGet());
    }

    @Override
    protected void onStop(Session session) {
        rejectFilter.clearCache(session);
        super.onStop(session);
        logger.info("Session destroy, Online#{}", ONLINE.decrementAndGet());
    }

    @Autowired
    public void setRejectFilter(RejectFilter rejectFilter) {
        this.rejectFilter = rejectFilter;
    }
}
