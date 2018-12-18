package com.linln.admin.order.controller;

import com.linln.admin.core.enums.ResultEnum;
import com.linln.admin.core.enums.StatusEnum;
import com.linln.admin.core.exception.ResultException;
import com.linln.admin.core.web.TimoExample;
import com.linln.admin.order.domain.Order;
import com.linln.admin.order.service.OrderService;
import com.linln.admin.order.validator.OrderForm;
import com.linln.core.utils.FormBeanUtil;
import com.linln.core.utils.ResultVoUtil;
import com.linln.core.vo.ResultVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/12/18
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 列表页面
     * @param pageIndex 页码
     * @param pageSize 获取数据长度
     */
    @GetMapping("/index")
    @RequiresPermissions("/order/index")
    public String index(Model model, Order order,
            @RequestParam(value="page",defaultValue="1") int pageIndex,
            @RequestParam(value="size",defaultValue="10") int pageSize){

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching();

        // 获取订单管理列表
        Example<Order> example = TimoExample.of(order, matcher);
        Page<Order> list = orderService.getPageList(example, pageIndex, pageSize);

        // 封装数据
        model.addAttribute("list",list.getContent());
        model.addAttribute("page",list);
        return "/order/order/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("/order/add")
    public String toAdd(){
        return "/order/order/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("/order/edit")
    public String toEdit(@PathVariable("id") Long id, Model model){
        Order order = orderService.getId(id);
        model.addAttribute("order",order);
        return "/order/order/add";
    }

    /**
     * 保存添加/修改的数据
     * @param orderForm 表单验证对象
     */
    @PostMapping({"/add","/edit"})
    @RequiresPermissions({"/order/add","/order/edit"})
    @ResponseBody
    public ResultVo save(@Validated OrderForm orderForm){

        // 将验证的数据复制给实体类
        Order order = new Order();
        if(orderForm.getId() != null){
            order = orderService.getId(orderForm.getId());
        }
        FormBeanUtil.copyProperties(orderForm, order);

        // 保存数据
        orderService.save(order);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("/order/detail")
    public String toDetail(@PathVariable("id") Long id, Model model){
        Order order = orderService.getId(id);
        model.addAttribute("order",order);
        return "/order/order/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("/order/status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> idList){
        try {
            // 获取状态StatusEnum对象
            StatusEnum statusEnum = StatusEnum.valueOf(param.toUpperCase());
            // 更新状态
            Integer count = orderService.updateStatus(statusEnum,idList);
            if (count > 0){
                return ResultVoUtil.success(statusEnum.getMessage()+"成功");
            }else{
                return ResultVoUtil.error(statusEnum.getMessage()+"失败，请重新操作");
            }
        } catch (IllegalArgumentException e){
            throw new ResultException(ResultEnum.STATUS_ERROR);
        }
    }

}
