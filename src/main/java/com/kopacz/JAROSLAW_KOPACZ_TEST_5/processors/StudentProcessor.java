package com.kopacz.JAROSLAW_KOPACZ_TEST_5.processors;

import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.Student;
import org.springframework.batch.item.ItemProcessor;

public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student student) throws Exception {
        return student;
    }
}
