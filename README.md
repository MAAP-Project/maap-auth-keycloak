MAAP Keycloak Event Listener provisioning Che organizations 
=================================================

Keycloak eventing can be extended via its [Event Listener SPI](https://www.keycloak.org/docs/latest/server_development/index.html#_events). This Keycloak extension implements a login event listener to provision users in the appropriate Che organization upon logging in to Keycloak/Che.

Deployment
----------
1. Copy target/maap-kc-<version>.jar to the `providers` folder in the Keycloak home directory (create folder if it doesn't exist).
2. Start (or restart) the Keycloak server. 
3. In the admin console, select your realm, then click on Events, followed by config. Click on Listeners select box, then pick `login-event-listener` from the dropdown. After this try to logout and 
login again to see events being generated by Keycloak.


