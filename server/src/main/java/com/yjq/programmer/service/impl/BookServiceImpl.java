package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.constant.CreditDescription;
import com.yjq.programmer.dao.BookMapper;
import com.yjq.programmer.dao.CreditItemMapper;
import com.yjq.programmer.dao.RentalItemMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.dao.my.MyRentalMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.BookStateEnum;
import com.yjq.programmer.enums.CreditStateEnum;
import com.yjq.programmer.enums.RentalItemStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IBookService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-09-21 14:41
 */
@Service
@Transactional
public class BookServiceImpl implements IBookService {

    @Resource
    private BookMapper bookMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RentalItemMapper rentalItemMapper;

    @Resource
    private MyRentalMapper myRentalMapper;

    @Resource
    private CreditItemMapper creditItemMapper;

    /**
     * 分页获取图书数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<BookDTO>> getBookListByPage(PageDTO<BookDTO> pageDTO) {
        BookExample bookExample = new BookExample();
        // 判断是否进行关键字搜索
        BookDTO searchBookDTO = pageDTO.getSearchEntity();
        BookExample.Criteria criteria = bookExample.createCriteria();
        if(!CommonUtil.isEmpty(searchBookDTO.getName())) {
            criteria.andNameLike("%" + searchBookDTO.getName() + "%");
        }
        if(searchBookDTO.getState() != 0) {
            criteria.andStateEqualTo(searchBookDTO.getState());
        }
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        if(pageDTO.getSize() == null) {
            pageDTO.setSize(5);
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出图书数据
        List<Book> bookList = bookMapper.selectByExample(bookExample);
        PageInfo<Book> pageInfo = new PageInfo<>(bookList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<BookDTO> bookDTOList = CopyUtil.copyList(bookList, BookDTO.class);
        pageDTO.setList(bookDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存图书数据(添加、修改)
     * @param bookDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveBook(BookDTO bookDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(bookDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        Book book = CopyUtil.copy(bookDTO, Book.class);
        if(CommonUtil.isEmpty(book.getId())){
            // id为空 说明是添加数据
            // 生成8位id
            book.setId(UuidUtil.getShortUuid());
            book.setCreateTime(new Date());
            // 添加数据到数据库
            if(bookMapper.insertSelective(book) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.BOOK_ADD_ERROR);
            }
        }else{
            // id不为空 说明是修改数据
            Book bookDB = bookMapper.selectByPrimaryKey(book.getId());
            if(BookStateEnum.OFF_SHELVES.getCode().equals(book.getState()) && BookStateEnum.RENTAL.getCode().equals(bookDB.getState())) {
                return ResponseDTO.errorByMsg(CodeMsg.BOOK_OFF_SHELVES_ERROR);
            }
            if(BookStateEnum.WAIT_RENTAL.getCode().equals(book.getState()) && BookStateEnum.RENTAL.getCode().equals(bookDB.getState())) {
                return ResponseDTO.errorByMsg(CodeMsg.BOOK_WAIT_RENTAL_ERROR);
            }
            // 修改数据库中数据
            if(bookMapper.updateByPrimaryKeySelective(book) == 0){
                return ResponseDTO.errorByMsg(CodeMsg.BOOK_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除图书数据
     * @param bookDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeBook(BookDTO bookDTO) {
        if(CommonUtil.isEmpty(bookDTO.getId())){
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除图书信息
        if(bookMapper.deleteByPrimaryKey(bookDTO.getId()) == 0){
            return ResponseDTO.errorByMsg(CodeMsg.BOOK_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 借阅图书操作
     * @param rentalItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> rentalBook(RentalItemDTO rentalItemDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(rentalItemDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        User user = userMapper.selectByPrimaryKey(rentalItemDTO.getUserId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if (user.getCreditRate() < 80) {
            return ResponseDTO.errorByMsg(CodeMsg.CREDIT_RENTAL_ERROR);
        }
        Book book = bookMapper.selectByPrimaryKey(rentalItemDTO.getBookId());
        if(book == null) {
            return ResponseDTO.errorByMsg(CodeMsg.BOOK_NOT_EXIST);
        }
        if(CommonUtil.differentDaysByMillisecond(new Date(), rentalItemDTO.getPredictTime()) > 180) {
            return  ResponseDTO.errorByMsg(CodeMsg.BOOK_RENTAL_TIME_OVER);
        }
        // 修改图书状态 乐观锁控制
        BookExample bookExample = new BookExample();
        bookExample.createCriteria().andIdEqualTo(book.getId()).andVersionEqualTo(book.getVersion());
        book.setState(BookStateEnum.RENTAL.getCode());
        book.setVersion(book.getVersion() + 1);
        if(bookMapper.updateByExampleSelective(book, bookExample) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.BOOK_EDIT_ERROR);
        }
        // 写入借阅详情信息
        RentalItem rentalItem = CopyUtil.copy(rentalItemDTO, RentalItem.class);
        rentalItem.setId(UuidUtil.getShortUuid());
        rentalItem.setCreateTime(new Date());
        rentalItem.setBookName(book.getName());
        rentalItem.setBookPhoto(book.getPhoto());
        rentalItem.setState(RentalItemStateEnum.RENTAL.getCode());
        if(rentalItemMapper.insertSelective(rentalItem) == 0) {
            throw new RuntimeException(CodeMsg.BOOK_RENTAL_ERROR.getMsg());
        }
        return ResponseDTO.successByMsg(true, "借阅成功！");
    }

    /**
     * 分页获取借阅数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<RentalItemDTO>> getRentalListByPage(PageDTO<RentalItemDTO> pageDTO) {
        Map<String, Object> queryMap = new HashMap<>();
        // 判断是否进行关键字搜索
        RentalItemDTO searchRentalItemDTO = pageDTO.getSearchEntity();
        queryMap.put("bookName", searchRentalItemDTO.getBookName());
        UserDTO searchUserDTO = searchRentalItemDTO.getUserDTO();
        queryMap.put("username", searchUserDTO.getUsername());
        User user = userMapper.selectByPrimaryKey(searchUserDTO.getId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if(RoleEnum.STUDENT.getCode().equals(user.getRoleId())) {
            queryMap.put("userId", user.getId());
        }
        queryMap.put("state", searchRentalItemDTO.getState());
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        if(pageDTO.getSize() == null) {
            pageDTO.setSize(5);
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出租赁数据
        List<RentalItem> rentalItemList = myRentalMapper.findRentalItemBySearch(queryMap);
        PageInfo<RentalItem> pageInfo = new PageInfo<>(rentalItemList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 讲domain类型数据  转成 DTO类型数据
        List<RentalItemDTO> rentalItemDTOList = CopyUtil.copyList(rentalItemList, RentalItemDTO.class);
        for(RentalItemDTO rentalItemDTO : rentalItemDTOList) {
            rentalItemDTO.setUserDTO(CopyUtil.copy(userMapper.selectByPrimaryKey(rentalItemDTO.getUserId()), UserDTO.class));
        }
        pageDTO.setList(rentalItemDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 删除借阅数据
     * @param rentalItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> removeRental(RentalItemDTO rentalItemDTO) {
        if(CommonUtil.isEmpty(rentalItemDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        if(rentalItemMapper.deleteByPrimaryKey(rentalItemDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.RENTAL_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true,  "删除成功！");
    }

    /**
     * 归还图书操作
     * @param rentalItemDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> returnBook(RentalItemDTO rentalItemDTO) {
        if(CommonUtil.isEmpty(rentalItemDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        RentalItem rentalItem = rentalItemMapper.selectByPrimaryKey(rentalItemDTO.getId());
        rentalItemDTO.setBookId(rentalItem.getBookId());
        rentalItemDTO.setUserId(rentalItem.getUserId());
        rentalItemDTO.setPredictTime(rentalItem.getPredictTime());
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(rentalItemDTO);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseDTO.errorByMsg(validate);
        }
        User user = userMapper.selectByPrimaryKey(rentalItem.getUserId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        // 判断借阅状态
        if(RentalItemStateEnum.ILLEGAL_RETURN.getCode().equals(rentalItemDTO.getState())) {
            // 归还异常，扣除信誉分
            if(user.getCreditRate() > 0) {
                user.setCreditRate(user.getCreditRate() - 5);
                userMapper.updateByPrimaryKeySelective(user);
                CreditItem creditItem = new CreditItem();
                creditItem.setId(UuidUtil.getShortUuid());
                creditItem.setCreateTime(new Date());
                creditItem.setRate(-5);
                creditItem.setState(CreditStateEnum.OVER.getCode());
                creditItem.setNowRate(user.getCreditRate());
                creditItem.setUserId(user.getId());
                creditItem.setDescription(CreditDescription.CREDIT_RATE_SUB_BY_RENTAL);
                if(creditItemMapper.insertSelective(creditItem) == 0) {
                    throw new RuntimeException(CodeMsg.CREDIT_ADD_ERROR.getMsg());
                }
            }
        } else if (RentalItemStateEnum.NORMAL_RETURN.getCode().equals(rentalItemDTO.getState())) {
            // 正常归还，增加信誉分
            if(user.getCreditRate() < 100) {
                user.setCreditRate(user.getCreditRate() + 5);
                userMapper.updateByPrimaryKeySelective(user);
                CreditItem creditItem = new CreditItem();
                creditItem.setId(UuidUtil.getShortUuid());
                creditItem.setCreateTime(new Date());
                creditItem.setRate(5);
                creditItem.setState(CreditStateEnum.OVER.getCode());
                creditItem.setNowRate(user.getCreditRate());
                creditItem.setUserId(user.getId());
                creditItem.setDescription(CreditDescription.CREDIT_RATE_ADD_BY_RENTAL);
                if(creditItemMapper.insertSelective(creditItem) == 0) {
                    throw new RuntimeException(CodeMsg.CREDIT_ADD_ERROR.getMsg());
                }
            }
        }
        // 更新图书状态
        Book book = bookMapper.selectByPrimaryKey(rentalItem.getBookId());
        if(book != null) {
            book.setState(BookStateEnum.WAIT_RENTAL.getCode());
            bookMapper.updateByPrimaryKeySelective(book);
        }
        // 更新借阅数据
        if(rentalItemMapper.updateByPrimaryKeySelective(CopyUtil.copy(rentalItemDTO, RentalItem.class)) == 0) {
            throw new RuntimeException(CodeMsg.RENTAL_RETURN_ERROR.getMsg());
        }
        return ResponseDTO.successByMsg(true, "归还成功！");
    }

    /**
     * 获取图书总数
     * @return
     */
    @Override
    public ResponseDTO<Integer> getBookTotal() {
        return ResponseDTO.success(bookMapper.countByExample(new BookExample()));
    }

    /**
     * 获取今日借阅数
     * @return
     */
    @Override
    public ResponseDTO<Integer> getRentalTotalByDay() {
        RentalItemExample rentalItemExample = new RentalItemExample();
        Date startDate = CommonUtil.getFormatterStr(CommonUtil.getFormatterDate(new Date(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date endDate = CommonUtil.getFormatterStr(CommonUtil.getFormatterDate(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        rentalItemExample.createCriteria().andCreateTimeBetween(startDate, endDate);
        int total = rentalItemMapper.countByExample(rentalItemExample);
        return ResponseDTO.success(total);
    }
}
