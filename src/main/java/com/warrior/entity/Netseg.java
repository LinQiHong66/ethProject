package com.warrior.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lqh
 * @since 2018-05-22
 */
@TableName("tb_netseg")
public class Netseg extends Model<Netseg> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 网段
     */
	@TableField("net_seg")
	private Integer netSeg;
	@TableField("start_time")
	private String startTime;
	@TableField("end_time")
	private String endTime;
	@TableField("use_time")
	private String useTime;


	public Integer getId() {
		return id;
	}

	public Netseg setId(Integer id) {
		this.id = id;
		return this;
	}

	public Integer getNetSeg() {
		return netSeg;
	}

	public Netseg setNetSeg(Integer netSeg) {
		this.netSeg = netSeg;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public Netseg setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public Netseg setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getUseTime() {
		return useTime;
	}

	public Netseg setUseTime(String useTime) {
		this.useTime = useTime;
		return this;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
