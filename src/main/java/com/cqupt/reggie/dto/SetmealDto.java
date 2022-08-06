package com.cqupt.reggie.dto;

import com.cqupt.reggie.entity.Setmeal;
import com.cqupt.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
