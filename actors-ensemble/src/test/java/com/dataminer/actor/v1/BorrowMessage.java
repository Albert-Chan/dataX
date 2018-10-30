package com.dataminer.actor.v1;

import com.dataminer.actor.v1.Actor;
import com.dataminer.actor.v1.Message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BorrowMessage extends Message {
	public BorrowMessage(Actor from, Actor to, String msg, int askFor) {
		super(from, to, msg);
		this.askFor = askFor;
	}

	private int askFor;

}
