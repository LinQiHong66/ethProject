package com.warrior.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
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
 * @since 2018-05-18
 */
@TableName("tb_log")
public class Log extends Model<Log> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	private String logName;
	private String logContent;
	private Date date;


	public Integer getId() {
		return id;
	}

	public Log setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getLogName() {
		return logName;
	}

	public Log setLogName(String logName) {
		this.logName = logName;
		return this;
	}

	public String getLogContent() {
		return logContent;
	}

	public Log setLogContent(String logContent) {
		this.logContent = logContent;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public Log setDate(Date date) {
		this.date = date;
		return this;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
