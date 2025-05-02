package com.shrimpshop.controller;

import com.shrimpshop.model.Shrimp;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/shrimp")
public class ShrimpController {
    private List<Shrimp> shrimpList = new ArrayList<>();

    @GetMapping
    public List<Shrimp> getAllShrimp() {
        return shrimpList;
    }

    @PostMapping
    public Shrimp addShrimp(@RequestBody Shrimp shrimp) {
        shrimpList.add(shrimp);
        return shrimp;
    }
}
