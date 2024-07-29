package com.mymongotest.springmongtest3.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Document("user")
public class Users{
    
	private Long id;
    private String title;
    private String message;
}