package com.cqupt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.reggie.common.CustomException;
import com.cqupt.reggie.dto.DishDto;
import com.cqupt.reggie.dto.SetmealDto;
import com.cqupt.reggie.entity.Dish;
import com.cqupt.reggie.entity.DishFlavor;
import com.cqupt.reggie.entity.Setmeal;
import com.cqupt.reggie.entity.SetmealDish;
import com.cqupt.reggie.mapper.SetmealMapper;
import com.cqupt.reggie.service.SetmealDishService;
import com.cqupt.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息setmeal
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存菜品的关联信息setmealDish
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id查询套餐信息和菜品信息
     *
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByIdWithDishes(Long id) {
        //查询套餐基本信息setmeal
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        //查询套餐菜品信息setmeal_dishes
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId, setmeal.getId());
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(dishes);

        return setmealDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(SetmealDto setmealDto) {
        //更改套餐信息
        this.updateById(setmealDto);
        //清理当前套餐对应菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getDishId, setmealDto.getId());

        setmealDishService.remove(queryWrapper);
        //添加提交过的菜品数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     *
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in(1,2,3) and status=1;
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int counts = this.count(queryWrapper);
        if(counts>0){
            //如果不能删除，抛出业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除，先删除套餐表中的数据--setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal id in (1,2,3);
        //删除关系表中的数据--setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getDishId,ids);

        setmealDishService.remove(queryWrapper1);

    }
}
