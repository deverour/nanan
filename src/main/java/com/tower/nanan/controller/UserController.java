package com.tower.nanan.controller;

import com.tower.nanan.entity.Result;
import com.tower.nanan.pojo.User;
import com.tower.nanan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*@RequestMapping("/login")
    public void login(User user, HttpSession httpSession, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            User loginUser = userService.findByUserAndPassword(user.getUsername(),user.getPassword());
            if(loginUser != null){
                //登录成功
                //将用户存入session

                httpServletRequest.setAttribute("login_msg","");
                httpSession.setAttribute("user",loginUser);
                System.out.println("登录成功  |   " +formatter.format(new Date()) );
                //跳转页面
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/pages/main.html");
            }else{
                //登录失败
                //提示信息
                httpServletRequest.setAttribute("login_msg","用户名或密码错误！");
                //跳转登录页面
                //httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
                //request.getRequestDispatcher(request.getContextPath()+"/bills/login.html").forward(request,response);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    @RequestMapping("/login")
    public Result login(@RequestBody User user, HttpSession httpSession, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            User loginUser = userService.findByUserAndPassword(user.getUsername(),user.getPassword());
            if(loginUser != null){
                //登录成功
                //将用户存入session

                httpServletRequest.setAttribute("login_msg","");
                httpSession.setAttribute("user",loginUser);
                System.out.println(user.getRegion()+" 登录成功  |   " +formatter.format(new Date()) );
                //httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/pages/main.html");
                return new Result(true,"登录成功");
                //跳转页面

            }else{
                //登录失败
                //提示信息
                httpServletRequest.setAttribute("login_msg","用户名或密码错误！");
                return new Result(false,"用户名或密码错误,请重试！");
                //跳转登录页面
                //httpServletResponse.sendRedirect(httpServletRequest.getContextPath());
                //request.getRequestDispatcher(request.getContextPath()+"/bills/login.html").forward(request,response);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
    }

    @RequestMapping("/group")
    public Result group(HttpSession httpSession){
        User user = (User)httpSession.getAttribute("user");
        return new Result(true,"",user);
    }

    @RequestMapping("/name")
    public Result name(HttpSession httpSession){
        User user = (User)httpSession.getAttribute("user");
        return new Result(true,"",user);
    }

    @RequestMapping("/change")
    public Result change(@RequestBody User user){

        try {
            int count = userService.change(user);
            if (count>0){
                return new Result(true,"修改密码成功，请重新登录");
            }else {
                return new Result(false,"修改密码失败，账号或密码错误");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"修改密码失败，请重试");
    }

    @RequestMapping("/logout")
    public Result logout(HttpSession httpSession){
        try {
            httpSession.setAttribute("user",null);
            return new Result(true,"退出登录成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"退出登录失败，请检查网络");
    }
}
