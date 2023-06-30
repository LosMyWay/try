package com.yjq.programmer.controller;

import com.yjq.programmer.dto.BookDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.RentalItemDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IBookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 蔡中宇
 * @create 2023-04-19
 */
@RequestMapping("/book")
@RestController
public class BookController {

    @Resource
    private IBookService bookService;

    /**
     * 分页获取图书数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<BookDTO>> getBookListByPage(@RequestBody PageDTO<BookDTO> pageDTO) {
        return bookService.getBookListByPage(pageDTO);
    }

    /**
     * 保存图书数据(添加、修改)
     * @param bookDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveBook(@RequestBody BookDTO bookDTO) {
        return bookService.saveBook(bookDTO);
    }

    /**
     * 后台删除图书数据
     * @param bookDTO
     * @return
     */
    @PostMapping("/remove")
    public ResponseDTO<Boolean> removeBook(@RequestBody BookDTO bookDTO) {
        return bookService.removeBook(bookDTO);
    }


    /**
     * 借阅图书操作
     * @param rentalItemDTO
     * @return
     */
    @PostMapping("/rental")
    public ResponseDTO<Boolean> rentalBook(@RequestBody RentalItemDTO rentalItemDTO) {
        return bookService.rentalBook(rentalItemDTO);
    }

    /**
     * 分页获取借阅数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/rentalList")
    public ResponseDTO<PageDTO<RentalItemDTO>> getRentalListByPage(@RequestBody PageDTO<RentalItemDTO> pageDTO) {
        return bookService.getRentalListByPage(pageDTO);
    }

    /**
     * 删除借阅数据
     * @param rentalItemDTO
     * @return
     */
    @PostMapping("/removeRental")
    public ResponseDTO<Boolean> removeRental(@RequestBody RentalItemDTO rentalItemDTO) {
        return bookService.removeRental(rentalItemDTO);
    }

    /**
     * 归还图书操作
     * @param rentalItemDTO
     * @return
     */
    @PostMapping("/return")
    public ResponseDTO<Boolean> returnBook(@RequestBody RentalItemDTO rentalItemDTO) {
        return bookService.returnBook(rentalItemDTO);
    }

    /**
     * 获取图书总数
     * @return
     */
    @PostMapping("/total")
    public ResponseDTO<Integer> getBookTotal() {
        return bookService.getBookTotal();
    }

    /**
     * 获取今日借阅数
     * @return
     */
    @PostMapping("/dayTotal")
    public ResponseDTO<Integer> getRentalTotalByDay() {
        return bookService.getRentalTotalByDay();
    }

}
