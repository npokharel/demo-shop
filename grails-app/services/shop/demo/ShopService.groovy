package shop.demo

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
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
        return Shop.findByDomain( SHOP_NAME + '.myshopify.com')?.email
    }

    def updateEmail (String email) {
        Shop shop = Shop.findByDomain( SHOP_NAME + '.myshopify.com')
        shop.email = email
        shop.save()
    }

    def saveShopifyFields() {

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

    /**
     * Returns the products containing tag or comma seperated tags
     * @param tag
     * @return
     */
    def getTaggedProducts ( String tag ) {

        //hashmap to get the max 250 results allowed per page to deal with the same product name string being present in lots of products in the store
        HashMap<String, String> queryParams = new HashMap<String, String>();

        JSONObject result = new JSONObject() //final result object
        JSONArray productsArray = new JSONArray()

        def count = shopifyClient.getProductService().getCount()

        //split tag's csv to string Array
        String [] tagArray
        boolean multiple = false
        if(tag.contains(",")){
            tagArray = tag.split(",")
            multiple = true
        }

        if (count > 250 ) {
            //TODO verify this logic queryParams stuffs
            def allProducts = shopifyClient.getProductService().getProducts(queryParams)
            JSONObject allJson = grails.converters.deep.JSON.parse(allProducts)

            long numLoops = count/250
            for (int i = 1; i<= numLoops; i++) {
                queryParams.put("page", i)
                queryParams.put("limit", "250")
                allJson = grails.converters.deep.JSON.parse(allProducts)
                result = new JSONObject()
                productsArray = new JSONArray()
                allJson.products.each {
                    if (multiple) {
                        tagArray.each{t->
                            if (it.tags.contains(t.trim())) productsArray.addAll(it)

                        }
                    }else {
                        if(it.tags.contains(tag)) productsArray.addAll(it)
                    }
                }
                result.put ('products', productsArray)
            }
        }else {

            queryParams.put("page", "1") //just the first page
            queryParams.put("limit", "250")

            def allProducts = shopifyClient.getProductService().getProducts(queryParams)
            JSONObject allJson = grails.converters.deep.JSON.parse(allProducts)

            allJson.products.each {
                if (multiple) {
                    tagArray.each{t->
                        if (it.tags.contains(t.trim())) productsArray.addAll(it)
                    }
                }else {
                    if(it.tags.contains(tag)) productsArray.addAll(it)
                }
            }
            result.put ('products', productsArray)
        }

        return result
    }
}