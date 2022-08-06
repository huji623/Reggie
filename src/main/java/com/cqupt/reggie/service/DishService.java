package com.cqupt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.reggie.dto.DishDto;
import com.cqupt.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入新增菜品的口味数据，需要操作dish和dish_flavor两张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和口味
    public DishDto getByIdWithFlavor(Long id);

    //根据id修改菜品信息和口味
    public void updateWithFlavor(DishDto dishDto);
}
