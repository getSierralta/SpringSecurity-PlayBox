package com.security.demo.student;

public class Student {

    private final Integer studentId;
    private final String studentName;

    public Student(Integer student, String studentName) {
        studentId = student;
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public Integer getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                '}';
    }
}
