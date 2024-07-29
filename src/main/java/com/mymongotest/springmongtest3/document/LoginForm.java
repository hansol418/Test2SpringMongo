package com.mymongotest.springmongtest3.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

public class LoginForm {
    private String id;
    private String password;
}
