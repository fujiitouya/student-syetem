package com.niudada.controller;

import com.niudada.entity.Student;
import com.niudada.entity.Teacher;
import com.niudada.entity.User;
import com.niudada.service.StudentService;
import com.niudada.service.TeacherService;
import com.niudada.service.UserService;
import com.niudada.utils.MD5Utils;
import com.niudada.utils.MapControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;

    //跳转登录页面
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //登录操作
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(String userName, String password, String captcha, String type, HttpSession session) {
        //判断用户名、密码、用户类型、验证码是否为空
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(captcha) || StringUtils.isEmpty(type)) {
            return MapControl.getInstance().error("用户名或密码不能为空").getMap();
        }
        //获取系统生成的验证码
        String _captcha = (String) session.getAttribute("captcha");
        //先判断验证码是否正确
        if (!(captcha.toLowerCase()).equals(_captcha.toLowerCase())) {
            //验证码错误
            return MapControl.getInstance().error("验证码错误").getMap();
        }

        //判断用户类型
        if ("1".equals(type)) { //管理员验证登录
            User user = userService.login(userName, MD5Utils.getMD5(password)); //对密码进行加密处理，因为数据库中存储的是加密后的密码
            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("type", 1);
                return MapControl.getInstance().success().add("data", user).getMap();
            } else {
                return MapControl.getInstance().error("用户名或密码错误").getMap();
            }
        }
        if ("2".equals(type)) { //老师验证登录
            Teacher teacher = teacherService.login(userName, MD5Utils.getMD5(password));
            if (teacher != null) {
                session.setAttribute("user", teacher);
                session.setAttribute("type", "2");
                return MapControl.getInstance().success().add("data", teacher).getMap();
            } else {
                return MapControl.getInstance().error("用户名或密码错误").getMap();
            }
        }
        if ("3".equals(type)) { //学生验证登录
            Student student = studentService.login(userName, MD5Utils.getMD5(password));
            if (student != null) {
                session.setAttribute("user", student);
                session.setAttribute("type", "3");
                return MapControl.getInstance().success().add("data", student).getMap();
            } else {
                return MapControl.getInstance().error("用户名或密码错误").getMap();
            }
        }
        return MapControl.getInstance().getMap();
    }

}
