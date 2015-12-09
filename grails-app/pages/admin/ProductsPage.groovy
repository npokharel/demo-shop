package admin

import common.BaseWebPage
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import grails.converters.deep.JSON
import shop.demo.ShopServiceInterface

public class ProductsPage extends BaseWebPage  {
    @SpringBean(name="shopService")
    ShopServiceInterface shopService

    public ProductsPage(final PageParameters parameters) {

        final RepeatingView repeating = new RepeatingView("repeating");
        add(repeating);

        String tag = parameters.get("tag")
        def products = shopService.getTaggedProducts(tag)
        def productJson = JSON.parse(products)

        productJson.products.each {

            AbstractItem item = new AbstractItem(repeating.newChildId())
            repeating.add(item)

            item.add(new Label("id", it.id.toString()))
            item.add(new Label("tags", it.tags))
            item.add(new Label("handle", it.handle))
            item.add(new Label("vendor", it.vendor))
            item.add(new Label("published_scope", it.published_scope))
            item.add(new Label("title", it.title))


        }


    }
}