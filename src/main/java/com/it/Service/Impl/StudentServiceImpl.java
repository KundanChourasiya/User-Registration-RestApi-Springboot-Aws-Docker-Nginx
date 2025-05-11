package com.it.Service.Impl;

import com.it.Entity.Student;
import com.it.GlobalException.UserNotFoundException;
import com.it.Payload.ApiResponse;
import com.it.Payload.StudentDto;
import com.it.Repository.StudentRepository;
import com.it.Service.StudentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {


    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // map to dto
    private StudentDto mapTODto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setPassword(student.getPassword());
        dto.setMobile(student.getMobile());
        dto.setGender(student.getGender());
        dto.setAge(student.getAge());
        return dto;
    }

    // map to Entity
    private Student mapToEntity(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setMobile(dto.getMobile());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        return student;
    }


    @Override
    public StudentDto createStudent(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setMobile(dto.getMobile());

        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("User email already exist.");
        }
        if (studentRepository.existsByMobile(dto.getMobile())) {
            throw new IllegalArgumentException("User Mobile no. already exist.");
        }

        Student saveStudent = studentRepository.save(student);
        StudentDto studentDto = mapTODto(saveStudent);
        return studentDto;
    }

    @Override
    public List<StudentDto> getAllStudent() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentDto> studentDtoList = studentList.stream().map(this::mapTODto).collect(Collectors.toUnmodifiableList());
        return studentDtoList;
    }

    @Override
    public Page<StudentDto> getAllStudentPage(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        List<StudentDto> studentDtoList = studentPage.getContent().stream().map(this::mapTODto).collect(Collectors.toList());
        return new PageImpl<>(studentDtoList, pageable, studentPage.getTotalPages());
    }

    @Override
    public StudentDto getStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        StudentDto studentDto = mapTODto(student);
        return studentDto;
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto dto) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        student.setId(id);
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setMobile(dto.getMobile());
        Student save = studentRepository.save(student);
        StudentDto studentDto = mapTODto(save);
        return studentDto;
    }

    @Override
    public StudentDto deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        studentRepository.deleteById(student.getId());
        StudentDto studentDto = mapTODto(student);
        return studentDto;
    }

    @Override
    public List<StudentDto> searchByName(String name) {
        List<Student> studentList = studentRepository.findByFullNameContainingIgnoreCase(name);
        List<StudentDto> studentDtoList = studentList.stream().map(this::mapTODto).collect(Collectors.toUnmodifiableList());
        return studentDtoList;
    }


}
