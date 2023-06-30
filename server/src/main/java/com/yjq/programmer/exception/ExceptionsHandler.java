package com.yjq.programmer.exception;


import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 蔡中宇
 * @create 2023-04-26
 */

/**
 * 运行时触发异常捕获
 */
@ControllerAdvice
public class ExceptionsHandler {

    private  final Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseDTO<Boolean> handle(RuntimeException e) {
        e.printStackTrace();
        if(!CommonUtil.isEmpty(e.getMessage())) {
            logger.info("异常信息={}", e.getMessage());
            if (CodeMsg.SEAT_PICK_ERROR.getMsg().equals(e.getMessage())) {
                return ResponseDTO.errorByMsg(CodeMsg.SEAT_PICK_ERROR);
            }
            if(CodeMsg.SEAT_STATE_EDIT_ERROR.getMsg().equals(e.getMessage())) {
                return ResponseDTO.errorByMsg(CodeMsg.SEAT_STATE_EDIT_ERROR);
            }
            if(CodeMsg.CREDIT_ADD_ERROR.getMsg().equals(e.getMessage())) {
                return ResponseDTO.errorByMsg(CodeMsg.CREDIT_ADD_ERROR);
            }
            if(CodeMsg.BOOK_RENTAL_ERROR.getMsg().equals(e.getMessage())) {
                return ResponseDTO.errorByMsg(CodeMsg.BOOK_RENTAL_ERROR);
            }
            if(CodeMsg.RENTAL_RETURN_ERROR.getMsg().equals(e.getMessage())) {
                return ResponseDTO.errorByMsg(CodeMsg.RENTAL_RETURN_ERROR);
            }
        }
        return ResponseDTO.errorByMsg(CodeMsg.SYSTEM_ERROR);
    }

}
