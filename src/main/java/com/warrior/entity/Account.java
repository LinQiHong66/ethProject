package com.warrior.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lqh
 * @since 2018-05-16
 */
@TableName("tb_account")
public class Account extends Model<Account> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 关联服务器id
     */
	private Integer serverId;
    /**
     * 钱包用户名
     */
	private String user;
    /**
     * 钱包密码
     */
	private String pwd;
    /**
     * 账户密码
     */
	private String accountPwd;
    /**
     * 钱包地址
     */
	private String address;
    /**
     * 钱包可用余额
     */
	private BigDecimal balance;


	public Integer getId() {
		return id;
	}

	public Account setId(Integer id) {
		this.id = id;
		return this;
	}

	public Integer getServerId() {
		return serverId;
	}

	public Account setServerId(Integer serverId) {
		this.serverId = serverId;
		return this;
	}

	public String getUser() {
		return user;
	}

	public Account setUser(String user) {
		this.user = user;
		return this;
	}

	public String getPwd() {
		return pwd;
	}

	public Account setPwd(String pwd) {
		this.pwd = pwd;
		return this;
	}

	public String getAccountPwd() {
		return accountPwd;
	}

	public Account setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public Account setAddress(String address) {
		this.address = address;
		return this;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Account setBalance(BigDecimal balance) {
		this.balance = balance;
		return this;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
