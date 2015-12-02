import org.apache.wicket.protocol.http.WebApplication;

import grails.util.*
import home.HomePage
import org.apache.wicket.spring.injection.annot.SpringComponentInjector
import org.apache.wicket.RuntimeConfigurationType

public class WicketApplication extends WebApplication {
    


    /**
     * Configures Grails' application context to be used for @SpringBean injection
     */
    /*protected void init() {
        super.init()
        //addComponentInstantiationListener(new SpringComponentInjector(this, ApplicationHolder.getApplication().getMainContext(), false));
        getComponentInstantiationListeners().add(new SpringComponentInjector(this)); //replaces above Spring injection in wicket 1.4

        //replaces mountBookmarkablePage() in wicket 1.4
        mount(new MountedMapper("/home1",HomePage.class, new UrlPathPageParametersEncoder()))
        mountPage("/home2",HomePage.class)

    }*/

    public void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

    }


    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    Class getHomePage() { HomePage.class }


    /**
     * If we're running in Grails development environment use Wicket development environment
     */

    public RuntimeConfigurationType getConfigurationType() {
        if(GrailsUtil.isDevelopmentEnv()) {
            return RuntimeConfigurationType.DEVELOPMENT
        }
        return RuntimeConfigurationType.DEPLOYMENT
    }
}