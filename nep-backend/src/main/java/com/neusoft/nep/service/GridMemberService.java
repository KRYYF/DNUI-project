package com.neusoft.nep.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nep.dto.GridMemberAqiSubmitDTO;
import com.neusoft.nep.dto.GridMemberLoginDTO;
import com.neusoft.nep.entity.GridMember;
import com.neusoft.nep.vo.GridFeedbackDetailVO;
import com.neusoft.nep.vo.GridFeedbackVO;
import com.neusoft.nep.vo.GridMemberLoginVO;

import java.util.List;


public interface GridMemberService {


    /**
     * PR1 原有接口
     */
    List<GridMember> getByRegion(
            Integer provinceId,
            Integer cityId
    );


    List<GridMember> getAll();


    GridMember getById(
            Integer gmId
    );



    /**
     * PR2 网格员登录
     */
    GridMemberLoginVO login(
            GridMemberLoginDTO dto
    );



    /**
     * 查询任务列表
     */
    Page<GridFeedbackVO> pageTasks(
            String token,
            Integer current,
            Integer size
    );



    /**
     * 查询任务详情
     */
    GridFeedbackDetailVO detail(
            String token,
            Integer afId
    );



    /**
     * 提交AQI反馈
     */
    void submitAqi(
            String token,
            GridMemberAqiSubmitDTO dto
    );

}