package com.it.Controller;

import com.it.Payload.ApiResponse;
import com.it.Payload.StudentDto;
import com.it.Service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //URL : http://localhost:8080/student/create
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody StudentDto dto) {
        StudentDto student = studentService.createStudent(dto);
        ApiResponse<?> apiResponse = new ApiResponse<>(true, "Student saved!!!", student);
        return ResponseEntity.status(200).body(apiResponse);
    }

    //URL : http://localhost:8080/student/all-student
    @GetMapping("/all-student")
    public ResponseEntity<ApiResponse<?>> getAllStudent() {
        List<StudentDto> allStudent = studentService.getAllStudent();
        ApiResponse<List<StudentDto>> allStudentDetails = new ApiResponse<>(true, "All student details", allStudent);
        return ResponseEntity.status(200).body(allStudentDetails);
    }

    //URL :  http://localhost:8080/student/allstudent
    //URL :  http://localhost:8080/student/allstudent?page=0
    //URL : http://localhost:8080/student/allstudent?page=0&size=2
    @GetMapping("/allstudent")
    public ResponseEntity<ApiResponse<?>> getAllStudentPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        Page<StudentDto> allStudentPage = studentService.getAllStudentPage(PageRequest.of(page, size));
        if (!allStudentPage.isEmpty()){
            ApiResponse<Page<StudentDto>> allStudentDetailsInPage = new ApiResponse<>(true, "All student details in page", allStudentPage);
            return ResponseEntity.status(200).body(allStudentDetailsInPage);
        }
        ApiResponse<Object> noStudentDetailsInPage  = new ApiResponse<>(false, "No student Found ", null);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())).body(noStudentDetailsInPage);
    }

    //URL : http://localhost:8080/student/get/3
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<?>> getStudent(@PathVariable Long id) {
        StudentDto student = studentService.getStudent(id);
        ApiResponse<StudentDto> studentDtoApiResponse = new ApiResponse<>(true, "Student record by Id", student);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.ACCEPTED.value())).body(studentDtoApiResponse);
    }

    //URL : http://localhost:8080/student/update/1
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto dto) {
        StudentDto studentDto = studentService.updateStudent(id, dto);
        ApiResponse<StudentDto> studentDtoApiResponse = new ApiResponse<>(true, "Student record update Successfully", studentDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.OK.value())).body(studentDtoApiResponse);
    }

    //URL : http://localhost:8080/student/delete/6
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteStudent(@PathVariable Long id) {
        StudentDto student = studentService.deleteStudent(id);
        ApiResponse<StudentDto> studentDtoApiResponse = new ApiResponse<>(true, "Student id " + student.getId() + " Delete Successfully", null);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.OK.value())).body(studentDtoApiResponse);
    }

    //URL : http://localhost:8080/student/search?name=k
    //URL : http://localhost:8080/student/search?name=sdghs
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchByName(@RequestParam(required = false) String name) {

        List<StudentDto> studentDtos = studentService.searchByName(name);
        if (!studentDtos.isEmpty()) {
            ApiResponse<List<StudentDto>> studentFoundByName = new ApiResponse<>(true, "Student Found by name", studentDtos);
            return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.FOUND.value())).body(studentFoundByName);
        }
        ApiResponse<Object> noStudentFoundByName = new ApiResponse<>(false, "No student Found by name", null);
        return ResponseEntity.status(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())).body(noStudentFoundByName);

    }


}
