package com.epam.aemtraining.impl;

import com.day.cq.wcm.api.PageEvent;
import com.day.cq.wcm.api.PageModification;
import com.epam.aemtraining.IVersionMakerConfig;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 *
 * Created by Aliaksandr_Li on 5/13/2016.
 */
@Component(immediate = true,metatype=true)
@Service(EventHandler.class)
@Properties({
    @Property(name = EventConstants.EVENT_TOPIC, value = PageEvent.EVENT_TOPIC)
})
public class PageContentVersionMaker implements EventHandler {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private Session session;
    @Reference
    SlingRepository repository;

    @Reference
    private IVersionMakerConfig config;

    public void handleEvent(Event event) {
        PageEvent pgEvent = PageEvent.fromEvent(event);
        Iterator<PageModification> modifications = pgEvent.getModifications();
        while (modifications.hasNext()) {
            PageModification modification = modifications.next();
            Pattern p = Pattern.compile(String.format("^%s/[^/]*$", config.getPathToHandle()));
            if (p.matcher(modification.getPath()).matches()&&!modification.getType().equals(PageModification.ModificationType.DELETED)){
                try {
                    session = repository.loginAdministrative(null);
                    session.getWorkspace().getVersionManager().checkin(modification.getPath());
                } catch (RepositoryException e) {
                    log.debug(e.getMessage());
                }finally {
                    session.logout();
                }
            }
        }
    }
}