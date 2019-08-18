package org.akj.springboot.dtx.resource;

import org.akj.springboot.dtx.bean.UserInfoResponse;
import org.akj.springboot.dtx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping("/{uid}")
    public UserInfoResponse getUserInfo(@PathVariable Integer uid) {
        return userService.getUserInfo(uid);
    }
}
