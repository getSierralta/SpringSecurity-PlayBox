package com.security.demo.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
//The @RequestMapping annotation narrows down the mapping to the post() method. With this, the post() method will handle requests to the url
//RequestMapping annotation we can apply on class level and as well as on method level
@RequestMapping("api/v1/students")
//All the mappings after this have the "api/v1/students" before them
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Maria"),
            new Student(2, "Joao"),
            new Student(3, "Juan")
    );//This is a shortcut from .add(new Student etc)

    //@GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).
    //GetMapping we can apply only on method level
    @GetMapping(path = "{studentId}")
    //the @PathVariable annotation can be used to handle template variables in the request URI mapping,  and use them as method parameters.
    public Student getStudent(@PathVariable("studentId") Integer studentId){
        //this is kinda the same as saying something like Select student where student id = studentId and if you don't find it throw and exception
        //the Stream API is used to process collections of objects
        //Stream filter(Predicate predicate) returns a stream consisting of the elements of this stream that match the given predicate
        //This is an intermediate operation. executing an intermediate operation such as filter() does not actually perform any filtering
        return STUDENTS.stream().filter(student -> studentId.equals(student.getStudentId())).findFirst()
                        .orElseThrow(() -> new IllegalStateException("Student "+ studentId + " does not exists"));


    }
}
