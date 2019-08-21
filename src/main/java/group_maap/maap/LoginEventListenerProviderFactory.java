package group_maap.maap;

import com.google.auto.service.AutoService;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

@JBossLog
@AutoService(EventListenerProviderFactory.class)
public class LoginEventListenerProviderFactory implements EventListenerProviderFactory {

    private static final String ID = "login-event-listener";

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new LoginEventListenerProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
        log.infof("init config={}", config);
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        log.infof("postInit factory={}", factory);
    }

    @Override
    public void close() {
        log.infof("close");
    }

    @Override
    public String getId() {
        return ID;
    }
}
