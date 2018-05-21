package com.warrior.serviceImpl;

import com.warrior.entity.Server;
import com.warrior.mapper.ServerMapper;
import com.warrior.service.IServerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lqh
 * @since 2018-05-16
 */
@Service
public class ServerServiceImpl extends ServiceImpl<ServerMapper, Server> implements IServerService {
	
}
