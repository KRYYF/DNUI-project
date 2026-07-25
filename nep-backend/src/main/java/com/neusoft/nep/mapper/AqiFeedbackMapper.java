package com.neusoft.nep.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.nep.entity.AqiFeedback;
import org.apache.ibatis.annotations.Param;

public interface AqiFeedbackMapper extends BaseMapper<AqiFeedback> {
    // 查询指派给当前网格员的任务分页
    Page<AqiFeedback> selectTaskPage(Page<AqiFeedback> page, @Param("gmId") Integer gmId);
    // 根据工单ID查询详情
    AqiFeedback selectDetail(@Param("afId") Integer afId);
}
