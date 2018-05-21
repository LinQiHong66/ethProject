package com.warrior.serviceImpl;

import com.warrior.entity.Log;
import com.warrior.mapper.LogMapper;
import com.warrior.service.ILogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lqh
 * @since 2018-05-18
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
	
}
