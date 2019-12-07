package learnprograming.academy;

public class Product
{
    int product_id ;
    int category_id ;
    String product_name ;

    public Product(int product_id , int category_id , String product_name)
    {
        this.product_id = product_id ;
        this.category_id = category_id ;
        this.product_name = product_name ;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
