package ecom.configuration;


import ecom.global.GlobalData;
import ecom.model.CustomUserDetail;
import ecom.model.User;
import ecom.repository.RoleRepository;
import ecom.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //retrieve User details
        Object principal = authentication.getPrincipal();
        if(principal instanceof CustomUserDetail){
            //System.out.println("CustomUserDetails : "+((CustomUserDetail) principal).getUsername());
            String userEmail = ((CustomUserDetail) principal).getUsername();
            GlobalData.LOGINUSER = new User(userRepository.findUserByEmail(userEmail).get());

            System.out.println(GlobalData.LOGINUSER.toString());
            System.out.println(authentication.getAuthorities());
        }else{
            System.out.println("Principal: "+principal.toString());
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
