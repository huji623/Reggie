package com.cqupt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.reggie.dto.SetmealDto;
import com.cqupt.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 查询套餐及对应菜品
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDishes(Long id);

    /**
     * 修改套餐
     * @param setmealDto
     */
    public void updateWithFlavor(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

}
