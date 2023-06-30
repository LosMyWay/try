package com.yjq.programmer.service;

import com.yjq.programmer.dto.BookDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.RentalItemDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public interface IBookService {

    // 分页获取图书数据
    ResponseDTO<PageDTO<BookDTO>> getBookListByPage(PageDTO<BookDTO> pageDTO);

    // 保存图书数据(添加、修改)
    ResponseDTO<Boolean> saveBook(BookDTO bookDTO);

    // 删除图书数据
    ResponseDTO<Boolean> removeBook(BookDTO bookDTO);

    // 借阅图书操作
    ResponseDTO<Boolean> rentalBook(RentalItemDTO rentalItemDTO);

    // 分页获取借阅数据
    ResponseDTO<PageDTO<RentalItemDTO>> getRentalListByPage(PageDTO<RentalItemDTO> pageDTO);

    // 删除借阅数据
    ResponseDTO<Boolean> removeRental(RentalItemDTO rentalItemDTO);

    // 归还图书操作
    ResponseDTO<Boolean> returnBook(RentalItemDTO rentalItemDTO);

    // 获取图书总数
    ResponseDTO<Integer> getBookTotal();

    // 获取今日借阅数
    ResponseDTO<Integer> getRentalTotalByDay();
}
