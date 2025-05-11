package com.it.Service;

import com.it.Payload.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    // create Student
    public StudentDto createStudent(StudentDto dto);

    // get All student Details
    public List<StudentDto> getAllStudent();

    // get All student in page Format
    public Page<StudentDto> getAllStudentPage(Pageable pageable);

    // get single Student details
    public StudentDto getStudent(Long id);

    // Update Student Details
    public StudentDto updateStudent(Long id, StudentDto dto);

    // Delete Student Details
    public StudentDto deleteStudent(Long id);

    // Search Student details using name
    public List<StudentDto> searchByName(String name);

}
