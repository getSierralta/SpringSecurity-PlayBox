package com.security.demo.student;


import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
//The @RequestMapping annotation narrows down the mapping to the post() method. With this, the post() method will handle requests to the url
//RequestMapping annotation we can apply on class level and as well as on method level
@RequestMapping("management/api/v1/students")
//All the mappings after this have the "management/api/v1/students" before them
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Maria"),
            new Student(2, "Joao"),
            new Student(3, "Juan")
    );//This is a shortcut from .add(new Student etc)

    @GetMapping //get request
    public List<Student> getAllStudents(){
        System.out.println("getAllStudents");
        return STUDENTS;
    }

    @PostMapping //post request
    //@RequestBody annotation is used to convert the body of the HTTP request and response with java class objects.
    // This annotation will use registered HTTP message converters in the process of converting/mapping HTTP request/response body with java objects.
    public void registerNewStudent(@RequestBody Student student){
        System.out.println("postStudent");
        System.out.println(student);
    }
    @DeleteMapping(path = "{studentId}") //delete request
    //Path is a shortcut takes the origin management/api/v1/students that we have in our RequestMapping
    //And pastes it to the end so it looks like this management/api/v1/students/{studentId}
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        //@PathVariable takes the variable we use in the request so you can use it freely like any variable
        //Example = management/api/v1/students/69 | studentId = 69
        System.out.println("deleteStudent");
        System.out.println(studentId);
    }

    @PutMapping(path = "{studentId}") //update request
    //Path is a shortcut takes the origin management/api/v1/students that we have in our RequestMapping
    //And pastes it to the end so it looks like this management/api/v1/students/{studentId}
    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student){
        //@PathVariable takes the variable we use in the request so you can use it freely like any variable
        //Example = management/api/v1/students/69 | studentId = 69
        //@RequestBody annotation is used to convert the body of the HTTP request and response with java class objects.
        // This annotation will use registered HTTP message converters in the process of converting/mapping HTTP request/response body with java objects.

        //print format
        System.out.println("updateStudent");
        System.out.printf("%s %s%n", studentId, student);
    }
}
