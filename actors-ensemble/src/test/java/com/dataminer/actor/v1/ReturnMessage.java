package com.dataminer.actor.v1;

import com.dataminer.actor.v1.Actor;
import com.dataminer.actor.v1.Message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnMessage extends Message {
	private int returnBack;

	public ReturnMessage(Actor from, Actor to, String msg, int returnBack) {
		super(from, to, msg);
		this.returnBack = returnBack;
	}
}