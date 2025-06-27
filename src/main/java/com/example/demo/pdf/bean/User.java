package com.example.demo.pdf.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * ClassName: User
 * Package: com.example.demo.pdf.bean
 * Description:
 *
 * @Author 张尔豪
 * @Create 2025/6/19 23:02
 * @Version 1.0
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private String id;

    private String username;

    public User() {
        this.id = UUID.randomUUID().toString();
    }
}
