package com.warrior.entity;

import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("tb_server")
public class Server extends Model<Server> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * ip
     */
	private String host;
    /**
     * port
     */
	private Integer port;
    /**
     * 0:telnet不通，1telnet通，2 EthApi通
     */
	private Integer pwd;


	public Integer getId() {
		return id;
	}

	public Server setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getHost() {
		return host;
	}

	public Server setHost(String host) {
		this.host = host;
		return this;
	}

	public Integer getPort() {
		return port;
	}

	public Server setPort(Integer port) {
		this.port = port;
		return this;
	}

	public Integer getPwd() {
		return pwd;
	}

	public Server setPwd(Integer pwd) {
		this.pwd = pwd;
		return this;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
