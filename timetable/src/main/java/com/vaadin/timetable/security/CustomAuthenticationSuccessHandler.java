package com.vaadin.timetable.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    String url = "jdbc:mysql://aauorfmbt136d0.cuz1bxluuufz.ap-south-1.rds.amazonaws.com:3306/liveTimetable ";
    String user = "dbms";
    String pwd = "Password_123";
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //do some logic here if you want something to be done whenever
        //the user successfully logs in.
        int userId=0;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((MyUserDetails) principal).getUsername();
            userId =  ((MyUserDetails) principal).getId();
        }


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pwd);
            Statement stmt  = con.createStatement();
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String datetime = myDateObj.format(myFormatObj);
            String sql = "insert into userLog (userId,loginTime) values('"+userId+"','"+datetime+"');";
            int rs = stmt.executeUpdate(sql);
//           UI.getCurrent().navigate("dashboard");
            redirectStrategy.sendRedirect(httpServletRequest,httpServletResponse,"/");
        }catch (Exception e){
            e.getLocalizedMessage();
        }
    }

}