package com.mymongotest.springmongtest3.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class User2DB {
	
	private ObjectId id;
    private String email;
    private String password;
	

}
