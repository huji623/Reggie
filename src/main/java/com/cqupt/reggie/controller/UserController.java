package com.cqupt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqupt.reggie.common.R;
import com.cqupt.reggie.entity.User;
import com.cqupt.reggie.service.UserService;
import com.cqupt.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            //生成随机四位验证码
            String code= ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);
            //需要将生成的验证码保存到Session
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }
        return R.error("手机验证码发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone=map.get("phone").toString();
        //获取验证码
        String code=map.get("code").toString();
        //获取session中的手机号和验证码
        Object codeInSession=session.getAttribute(phone);
        //进行验证码比对（页面提交的手机号和验证码和session中保存的进行比较）
        if(codeInSession!=null&&codeInSession.equals(code)){
            //比对成功进行登录
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user==null){
            //判断当前手机号对应的用户是否为新用户，若微信用户就自动完成注册
                //判断当前手机号对应用户为新用户，创建新用户
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
