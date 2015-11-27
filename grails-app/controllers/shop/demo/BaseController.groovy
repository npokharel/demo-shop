package shop.demo

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import shopify.api.ShopifyClient

class BaseController {

    String SHOPIFY_API_KEY = "00cb355000deab70ca7a45564fc5689b"
    String SHOPIFY_API_SECRET = "e338bba1d3e7017026bd6b000b409b32"
    String SHOP_NAME = "narens-shop"
    String PERMANENT_TOKEN = "ed9ddf4a8b96f944fe58e9fbfb5c5df2"
    String email

    def grabEmail = {
        ShopifyClient shopifyClient = new ShopifyClient(
                SHOPIFY_API_KEY,
                SHOPIFY_API_SECRET,
                SHOP_NAME,
                PERMANENT_TOKEN
        )

        String responseText = shopifyClient.getShopService().getShop()
        JSONObject productJSON = JSON.parse(responseText)

        productJSON.shop.each {  key, v ->
            if (key.equals ("email")) {
                println key + " -- " +  v
                email = v
            }
        }

        if (!Shop.findByName(SHOP_NAME)) {
            Shop shop = new Shop( name: SHOP_NAME, email: email)
            shop.save()
        }
    }

}
