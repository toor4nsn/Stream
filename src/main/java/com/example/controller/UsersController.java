package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Cinema;
import com.example.entity.Users;

import com.example.service.impl.UsersServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (users)表控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/users")
public class UsersController {
    /**
     * 服务对象
     */
    @Resource
    private UsersServiceImpl usersServiceImpl;


    @GetMapping(value = "/{id}")
    public ResponseEntity<Users> getById(@PathVariable("id") String id) {
        return new ResponseEntity<>(usersServiceImpl.getById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/list")
    public ResponseEntity<Page<Users>> list(@RequestParam(required = false) Integer current,
                                            @RequestParam(required = false) Integer pageSize,
                                            @RequestBody(required = false) Users users) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
//        Page<Users> aPage = usersServiceImpl.page(new Page<>(current, pageSize));
        Page<Users> usersPage = usersServiceImpl.queryList(users, current, pageSize);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }

    @GetMapping(value = "queryByName")
    public ResponseEntity<List<Users>> queryListByName(@RequestParam("name") String name) {
        return new ResponseEntity<>(usersServiceImpl.queryListByName(name), HttpStatus.OK);
    }
}
