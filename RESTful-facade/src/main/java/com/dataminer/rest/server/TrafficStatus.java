package com.dataminer.rest.server;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TrafficStatus {

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	private long roadId;
	private Timestamp time;
	private float avgSpeed;
	private float avgDuration;
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