package com.example.demo.pdf.repository;

/**
 * ClassName: UserRepository
 * Package: com.example.demo.pdf.repository
 * Description:
 *
 * @Author 张尔豪
 * @Create 2025/6/19 23:01
 * @Version 1.0
 */
import com.example.demo.pdf.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
