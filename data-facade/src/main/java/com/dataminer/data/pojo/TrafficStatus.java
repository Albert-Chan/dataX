package com.dataminer.data.pojo;

import java.sql.Timestamp;

import com.alibaba.fastjson.annotation.JSONField;

public class TrafficStatus {
	@JSONField(name = "roadId")
	private long roadId;
	
	@JSONField(name = "time")
	private Timestamp time;
	
	@JSONField(name = "avgSpeed")
	private float avgSpeed;
	
	@JSONField(name = "avgDuration")
	private float avgDuration;
	
	@JSONField(name = "status")
	private int status;

	public long getRoadId() {
		return roadId;
	}

	public void setRoadId(long roadId) {
		this.roadId = roadId;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public float getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(float avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public float getAvgDuration() {
		return avgDuration;
	}

	public void setAvgDuration(float avgDuration) {
		this.avgDuration = avgDuration;
	}

}