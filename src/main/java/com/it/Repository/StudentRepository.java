package com.it.Repository;

import com.it.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);


    List<Student> findByFullNameContainingIgnoreCase(String fullname);

}