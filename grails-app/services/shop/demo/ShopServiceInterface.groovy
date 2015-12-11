package shop.demo

interface ShopServiceInterface {
    def saveEmail(Shop shop)

    def getEmail()

    def updateEmail (String email)

    def saveShopifyFields()

    def getTaggedProducts (String tag)
}