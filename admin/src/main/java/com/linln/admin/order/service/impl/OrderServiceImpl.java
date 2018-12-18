package com.linln.admin.order.service.impl;

import com.linln.admin.core.enums.StatusEnum;
import com.linln.admin.order.domain.Order;
import com.linln.admin.order.repository.OrderRepository;
import com.linln.admin.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/12/18
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 根据订单管理ID查询订单管理数据
     * @param id 订单管理ID
     */
    @Override
    @Transactional
    public Order getId(Long id) {
        Byte[] status = {StatusEnum.OK.getCode(), StatusEnum.FREEZED.getCode()};
        return orderRepository.findByIdAndStatusIn(id, status);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @param pageIndex 页码
     * @param pageSize 获取列表长度
     * @return 返回分页数据
     */
    @Override
    public Page<Order> getPageList(Example<Order> example, Integer pageIndex, Integer pageSize) {
        // 创建分页对象
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        PageRequest page = PageRequest.of(pageIndex-1, pageSize, sort);
        Page<Order> list = orderRepository.findAll(example, page);
        return list;
    }

    /**
     * 保存订单管理
     * @param order 订单管理实体类
     */
    @Override
    public Order save(Order order){
        return orderRepository.save(order);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Integer updateStatus(StatusEnum statusEnum, List<Long> idList){
        return orderRepository.updateStatus(statusEnum.getCode(),idList);
    }
}

