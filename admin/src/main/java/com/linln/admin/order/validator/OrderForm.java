package com.linln.admin.order.validator;

import com.linln.admin.order.domain.Order;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @author 小懒虫
 * @date 2018/12/18
 */
@Data
public class OrderForm extends Order implements Serializable {
}
