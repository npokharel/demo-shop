package home
import common.BaseWebPage

import admin.ProductsPage

import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.util.value.ValueMap
import shop.demo.ShopServiceInterface

import org.apache.wicket.spring.injection.annot.SpringBean

public class HomePage extends BaseWebPage {

    public HomePage(final PageParameters parameters) {

        add(new LoginForm("loginForm"));
    }

}

public class LoginForm extends Form{

    @SpringBean(name="shopService")
    ShopServiceInterface shopService

    ValueMap properties = new ValueMap();

    public String getEmail () {
        if (shopService.getEmail()) {
            return  shopService.getEmail()
        }
    }

    public LoginForm(String id){
        super(id);
        // shopService.saveShopifyFields()
        // TextField passwordField = new TextField("password", new PropertyModel(properties,"password"))
        TextField emailField = new TextField("email", new PropertyModel(properties,"email"))
        add(emailField)
        //TextField<String> username = new TextField<String>("username", Model.of(""));
        String emailStr = getEmail() //"email"
        Label emailLabel = new Label("emailLabel", "Current email : " + emailStr)
        add(emailLabel)
        Label enterEmail = new Label ("enterEmail", "Enter new value ")
        add(enterEmail)

        Label tagLabel = new Label("tagLabel", "Enter tag")
        add(tagLabel)
        TextField enterTag = new TextField("enterTag", new PropertyModel(properties,"enterTag"))
        add(enterTag)

    }

    @Override
    public void onSubmit(){
        //String passwordText = properties.getString("password");
        String emailText = properties.getString("email");
        String tagText = properties.getString("enterTag")
        println("tagText TEXT ENTERED: " + tagText);

        if(tagText != null ) {
            setResponsePage(ProductsPage.class ,  new PageParameters("tag=" + tagText))
        }else {
            error( " Email cannot be blank !!!")
        }

        /*if(passwordText != null && passwordText.equalsIgnoreCase("testing")){
            setResponsePage(CategoriesPage.class);
        } else{
            error("BOING!");
        }*/
    }
}