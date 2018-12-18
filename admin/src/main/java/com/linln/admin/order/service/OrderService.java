package com.linln.admin.order.service;

import com.linln.admin.core.enums.StatusEnum;
import com.linln.admin.order.domain.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/12/18
 */
public interface OrderService {

    Page<Order> getPageList(Example<Order> example, Integer pageIndex, Integer pageSize);

    Order getId(Long id);

    Order save(Order order);

    @Transactional
    Integer updateStatus(StatusEnum statusEnum, List<Long> idList);
}

