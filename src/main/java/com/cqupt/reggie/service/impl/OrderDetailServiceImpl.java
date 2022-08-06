package com.cqupt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.reggie.entity.OrderDetail;
import com.cqupt.reggie.mapper.OrderDetailMapper;
import com.cqupt.reggie.service.OrderDetaiService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetaiService {
}
