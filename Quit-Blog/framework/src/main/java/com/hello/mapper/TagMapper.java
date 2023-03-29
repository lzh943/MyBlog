package com.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.domain.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
