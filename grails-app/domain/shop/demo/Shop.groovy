package shop.demo

class Shop {

    String name
    String email
    String customerEmail
    String domain
    String myshopifyDomain
    String planName
    String planDisplayName

    static constraints = {
        customerEmail (nullable: true)
    }
}
