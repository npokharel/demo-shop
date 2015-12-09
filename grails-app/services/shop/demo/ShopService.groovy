package shop.demo

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject
import shopify.api.ShopifyClient
class ShopService implements ShopServiceInterface {

    static transactional = true

    String SHOPIFY_API_KEY = "00cb355000deab70ca7a45564fc5689b"
    String SHOPIFY_API_SECRET = "e338bba1d3e7017026bd6b000b409b32"
    String SHOP_NAME = "narens-shop"
    String PERMANENT_TOKEN = "ed9ddf4a8b96f944fe58e9fbfb5c5df2"


    ShopifyClient shopifyClient = new ShopifyClient(
            SHOPIFY_API_KEY,
            SHOPIFY_API_SECRET,
            SHOP_NAME,
            PERMANENT_TOKEN
    )

    def saveEmail ( Shop shop) {
        shop.save()
    }

    def getEmail ( ) {
        //return Shop.get(1)?.email
        return Shop.findByDomain( SHOP_NAME + '.myshopify.com')?.email
    }

    def updateEmail (String email) {
        Shop shop = Shop.findByDomain( SHOP_NAME + '.myshopify.com')
        shop.email = email
        shop.save()
    }

    def saveShopifyFields() {

        /*ShopifyClient shopifyClient = new ShopifyClient(
                SHOPIFY_API_KEY,
                SHOPIFY_API_SECRET,
                SHOP_NAME,
                PERMANENT_TOKEN
        )*/

        String responseText = shopifyClient.getShopService().getShop()
        JSONObject productJSON = JSON.parse(responseText)


        Shop shop = new Shop(
                name: productJSON.shop?.name,
                email: productJSON.shop?.email,
                customerEmail: productJSON.shop.customer_email == 'null' ? productJSON.shop.customer_email : "",
                domain: productJSON.shop?.domain,
                myshopifyDomain: productJSON.shop?.myshopify_domain,
                planName: productJSON.shop?.plan_name,
                planDisplayName: productJSON?.shop?.plan_display_name
        )

        if (!Shop.findByDomain( SHOP_NAME + '.myshopify.com')) {
            shop.save(failOnError: true)
        }

    }

    def getProducts (  ) {

        def queryParams = [
                "tags" : "niraj"
        ]
        //String responseText = shopifyClient.getShopService().getShop()
        String responseText = shopifyClient.getProductService().searchProducts(queryParams)
        JSONObject productJSON = JSON.parse(responseText)
        println productJSON.toString(4)
    }

    def getTaggedProducts ( String tag) {
        //def responseText =
        return shopifyClient.getProductService().taggedProducts( tag )
        //JSONObject productJSON = JSON.parse(responseText)
        //return productJSON.toString()
    }
}