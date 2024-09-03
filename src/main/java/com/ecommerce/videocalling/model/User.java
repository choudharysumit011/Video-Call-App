package com.ecommerce.videocalling.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "video-call")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class User {
    @Id
    private String username;
    private String email;
    private String password;
    private String status;
}