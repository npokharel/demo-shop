package common;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;


public class BaseWebPage extends WebPage {

    public BaseWebPage(){
        add(new FeedbackPanel("feedbackPanel"));
    }

}
