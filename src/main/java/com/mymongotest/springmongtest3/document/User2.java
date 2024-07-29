package com.mymongotest.springmongtest3.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Document("user2")
public class User2 {
    
	@Id
	private ObjectId id;
	
	@Indexed(unique = true)
    private String email;
	
    private String password;
    private String role;
    // 수강한 과목 리스트 넣을 예정.
}