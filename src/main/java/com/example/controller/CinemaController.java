package com.example.controller;

import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.service.CinemaService;
import com.example.entity.Cinema;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * movie rating 前端控制器
 * </p>
 *
 * @author zephyr
 * @since 2024-01-02
 */
@RestController
@RequestMapping("/cinema")
public class CinemaController {


    @Autowired
    private CinemaService cinemaService;

    @GetMapping(value = "/list")
    public ResponseEntity<Page<Cinema>> list(@RequestParam(required = false) Integer current, @RequestParam(required = false) Integer pageSize) {
        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Page<Cinema> aPage = cinemaService.page(new Page<>(current, pageSize));
        return new ResponseEntity<>(aPage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cinema> getById(@PathVariable("id") String id) {
        return new ResponseEntity<>(cinemaService.getById(id), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Object> create(@RequestBody Cinema params) {
        cinemaService.save(params);
        return new ResponseEntity<>("created successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        cinemaService.removeById(id);
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/update")
    public ResponseEntity<Object> update(@RequestBody Cinema params) {
        cinemaService.updateById(params);
        return new ResponseEntity<>("updated successfully", HttpStatus.OK);
    }
}
