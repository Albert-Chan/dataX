package com.dataminer.actor.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

 
@Data
@AllArgsConstructor
//@RequiredArgsConstructor(staticName = "of")
public class Message {
	@NonNull private final Actor from;
	@NonNull private final Actor to;
	private final String msg;
	
}
