package com.mymongotest.lunchmenu.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Document("lunch")
public class LunchMenu {

    private Long id;
    private String foodName;
    private String writer;
}