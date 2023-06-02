package ecom.global;

import ecom.model.CustomUserDetail;
import ecom.model.Product;
import ecom.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    public static List<Product> cart;
    static {
        cart = new ArrayList<Product>();
    }

    public static User LOGINUSER;
    static{
        LOGINUSER = new User();
    }
}
