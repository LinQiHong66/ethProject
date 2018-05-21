package com.warrior.serviceImpl;

import com.warrior.entity.Account;
import com.warrior.mapper.AccountMapper;
import com.warrior.service.IAccountService;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {
	
}
