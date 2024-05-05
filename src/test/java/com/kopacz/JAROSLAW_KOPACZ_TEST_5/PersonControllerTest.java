package com.kopacz.JAROSLAW_KOPACZ_TEST_5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.ClearContext;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.config.PersonsTest;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.User;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.UserRole;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.models.command.*;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.repository.UserRepository;
import com.kopacz.JAROSLAW_KOPACZ_TEST_5.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@PersonsTest
public class PersonControllerTest extends BaseIT {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    @Autowired
    public PersonControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                                  JwtService jwtService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }

    @Test
    void badType_shouldBadRequest() throws Exception {

        mockMvc.perform(get("/person/find?type=efew"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid type"));

    }

    @Test
    void emptyParameter_shouldBadRequest() throws Exception {

        mockMvc.perform(get("/person/find"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing type"));

    }

    @Test
    void shouldReturnsStudentsWithPageSize3() throws Exception {

        mockMvc.perform(get("/person/find?type=student&size=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954781"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].college").value("Harvard University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1500.00))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[1].college").value("Stanford University"))
                .andExpect(jsonPath("$[1].academicYear").value(4))
                .andExpect(jsonPath("$[1].scholarship").value(1800.00))
                .andExpect(jsonPath("$[2].firstName").value("Michael"))
                .andExpect(jsonPath("$[2].lastName").value("Johnson"))
                .andExpect(jsonPath("$[2].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[2].height").value(190.0))
                .andExpect(jsonPath("$[2].weight").value(80.0))
                .andExpect(jsonPath("$[2].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[2].college").value("MIT"))
                .andExpect(jsonPath("$[2].academicYear").value(2))
                .andExpect(jsonPath("$[2].scholarship").value(1200.00));

    }

    @Test
    void shouldReturnsStudentsWithPageSize3andNextPage() throws Exception {

        mockMvc.perform(get("/person/find?type=student&size=3&page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Emily"))
                .andExpect(jsonPath("$[0].lastName").value("Williams"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954784"))
                .andExpect(jsonPath("$[0].height").value(170.0))
                .andExpect(jsonPath("$[0].weight").value(65.0))
                .andExpect(jsonPath("$[0].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[0].college").value("Yale University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1600.00))
                .andExpect(jsonPath("$[1].firstName").value("David"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[1].height").value(175.0))
                .andExpect(jsonPath("$[1].weight").value(70.0))
                .andExpect(jsonPath("$[1].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[1].college").value("Columbia University"))
                .andExpect(jsonPath("$[1].academicYear").value(4))
                .andExpect(jsonPath("$[1].scholarship").value(2000.00));
    }

    @Test
    void shouldReturnsStudentsWithFirstnameEmily() throws Exception {

        mockMvc.perform(get("/person/find?type=student&FirstName=emily"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Emily"))
                .andExpect(jsonPath("$[0].lastName").value("Williams"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954784"))
                .andExpect(jsonPath("$[0].height").value(170.0))
                .andExpect(jsonPath("$[0].weight").value(65.0))
                .andExpect(jsonPath("$[0].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[0].college").value("Yale University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1600.00));
    }

    @Test
    void shouldReturnsStudentsWithLastnameBrown() throws Exception {

        mockMvc.perform(get("/person/find?type=student&LastName=brown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));;
    }

    @Test
    void shouldReturnsStudentsHeightFrom180() throws Exception {

        mockMvc.perform(get("/person/find?type=student&heightFrom=180"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954781"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].college").value("Harvard University"))
                .andExpect(jsonPath("$[0].academicYear").value(3))
                .andExpect(jsonPath("$[0].scholarship").value(1500.00))
                .andExpect(jsonPath("$[1].firstName").value("Michael"))
                .andExpect(jsonPath("$[1].lastName").value("Johnson"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[1].height").value(190.0))
                .andExpect(jsonPath("$[1].weight").value(80.0))
                .andExpect(jsonPath("$[1].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[1].college").value("MIT"))
                .andExpect(jsonPath("$[1].academicYear").value(2))
                .andExpect(jsonPath("$[1].scholarship").value(1200.00));
    }

    @Test
    void shouldReturnsStudentsHeightTo170() throws Exception {

        mockMvc.perform(get("/person/find?type=student&heightTo=170"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00))
                .andExpect(jsonPath("$[1].firstName").value("Emily"))
                .andExpect(jsonPath("$[1].lastName").value("Williams"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954784"))
                .andExpect(jsonPath("$[1].height").value(170.0))
                .andExpect(jsonPath("$[1].weight").value(65.0))
                .andExpect(jsonPath("$[1].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[1].college").value("Yale University"))
                .andExpect(jsonPath("$[1].academicYear").value(3))
                .andExpect(jsonPath("$[1].scholarship").value(1600.00));
    }

    @Test
    void shouldReturnsStudentsHWeightFrom80() throws Exception {

        mockMvc.perform(get("/person/find?type=student&weightFrom=80"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[0].height").value(190.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[0].college").value("MIT"))
                .andExpect(jsonPath("$[0].academicYear").value(2))
                .andExpect(jsonPath("$[0].scholarship").value(1200.00));
    }

    @Test
    void shouldReturnsStudentsHWeightTo60() throws Exception {

        mockMvc.perform(get("/person/find?type=student&weightTo=60"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00));
    }

    @Test
    void shouldReturnsStudentsByPeselnumber() throws Exception {

        mockMvc.perform(get("/person/find?type=student&peselNumber=73620954785"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));

    }

    @Test
    void shouldReturnsStudentsByEmail() throws Exception {

        mockMvc.perform(get("/person/find?type=student&email=davidbrown@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));

    }

    @Test
    void shouldReturnsStudentsByCollege() throws Exception {

        mockMvc.perform(get("/person/find?type=student&college=columbia university"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));

    }

    @Test
    void shouldReturnsStudentsAcademicYearFrom4() throws Exception {

        mockMvc.perform(get("/person/find?type=student&academicYearFrom=4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00))
                .andExpect(jsonPath("$[1].firstName").value("David"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[1].height").value(175.0))
                .andExpect(jsonPath("$[1].weight").value(70.0))
                .andExpect(jsonPath("$[1].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[1].college").value("Columbia University"))
                .andExpect(jsonPath("$[1].academicYear").value(4))
                .andExpect(jsonPath("$[1].scholarship").value(2000.00));
    }

    @Test
    void shouldReturnsStudentsAcademicYearTo2() throws Exception {

        mockMvc.perform(get("/person/find?type=student&academicYearTo=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[0].height").value(190.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[0].college").value("MIT"))
                .andExpect(jsonPath("$[0].academicYear").value(2))
                .andExpect(jsonPath("$[0].scholarship").value(1200.00));
    }

    @Test
    void shouldReturnsStudentsScholarshipFrom2000() throws Exception {

        mockMvc.perform(get("/person/find?type=student&scholarshipFrom=2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("David"))
                .andExpect(jsonPath("$[0].lastName").value("Brown"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954785"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(70.0))
                .andExpect(jsonPath("$[0].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[0].college").value("Columbia University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(2000.00));
    }

    @Test
    void shouldReturnsStudentsScholarshipTo1200() throws Exception {

        mockMvc.perform(get("/person/find?type=student&scholarshipTo=1200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954783"))
                .andExpect(jsonPath("$[0].height").value(190.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[0].college").value("MIT"))
                .andExpect(jsonPath("$[0].academicYear").value(2))
                .andExpect(jsonPath("$[0].scholarship").value(1200.00));
    }

    @Test
    void shouldReturnsEmployeesWorkDateFrom20220309() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&workStartDateFrom=2022-03-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Emma"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("83040445678"))
                .andExpect(jsonPath("$[0].height").value(170.0))
                .andExpect(jsonPath("$[0].weight").value(65.0))
                .andExpect(jsonPath("$[0].email").value("emmajohnson@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-04-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Analyst"))
                .andExpect(jsonPath("$[0].salary").value(5500.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(4))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0))
                .andExpect(jsonPath("$[1].firstName").value("Michael"))
                .andExpect(jsonPath("$[1].lastName").value("Williams"))
                .andExpect(jsonPath("$[1].peselNumber").value("84050556789"))
                .andExpect(jsonPath("$[1].height").value(185.0))
                .andExpect(jsonPath("$[1].weight").value(90.0))
                .andExpect(jsonPath("$[1].email").value("michaelwilliams@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-05-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Engineer"))
                .andExpect(jsonPath("$[1].salary").value(7000.00))
                .andExpect(jsonPath("$[1].numberOfProfessions").value(5))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0));

    }

    @Test
    void shouldReturnsEmployeesWorkDateTo20220209() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&workStartDateTo=2022-02-09"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("80010112345"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-01-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Programmer"))
                .andExpect(jsonPath("$[0].salary").value(5000.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0))
                .andExpect(jsonPath("$[1].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[1].salary").value(4500.00))
                .andExpect(jsonPath("$[1].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesActualProfessionDesigner() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&actualProfession=designer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[0].salary").value(4500.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesSalaryFrom7000() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&salaryFrom=7000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Williams"))
                .andExpect(jsonPath("$[0].peselNumber").value("84050556789"))
                .andExpect(jsonPath("$[0].height").value(185.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("michaelwilliams@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-05-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Engineer"))
                .andExpect(jsonPath("$[0].salary").value(7000.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(5))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesSalaryTo5000() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&salaryTo=5000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("80010112345"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-01-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Programmer"))
                .andExpect(jsonPath("$[0].salary").value(5000.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0))
                .andExpect(jsonPath("$[1].firstName").value("Alice"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[1].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[1].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[1].salary").value(4500.00))
                .andExpect(jsonPath("$[1].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[1].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesNumberOfProfessionsFrom5() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&numberOfProfessionsFrom=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Williams"))
                .andExpect(jsonPath("$[0].peselNumber").value("84050556789"))
                .andExpect(jsonPath("$[0].height").value(185.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("michaelwilliams@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-05-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Engineer"))
                .andExpect(jsonPath("$[0].salary").value(7000.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(5))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsEmployeesNumberOfProfessionsTo1() throws Exception {

        mockMvc.perform(get("/person/find?type=employee&numberOfProfessionsTo=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("80010112345"))
                .andExpect(jsonPath("$[0].height").value(175.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-01-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Programmer"))
                .andExpect(jsonPath("$[0].salary").value(5000.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    void shouldReturnsPensionersWorkYearsFrom40() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&workYearsFrom=40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("88020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2800.00))
                .andExpect(jsonPath("$[0].workYears").value(40))
                .andExpect(jsonPath("$[1].firstName").value("Michael"))
                .andExpect(jsonPath("$[1].lastName").value("Johnson"))
                .andExpect(jsonPath("$[1].peselNumber").value("95030334567"))
                .andExpect(jsonPath("$[1].height").value(190.0))
                .andExpect(jsonPath("$[1].weight").value(80.0))
                .andExpect(jsonPath("$[1].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[1].pensionValue").value(3000.00))
                .andExpect(jsonPath("$[1].workYears").value(45))
                .andExpect(jsonPath("$[2].firstName").value("David"))
                .andExpect(jsonPath("$[2].lastName").value("Brown"))
                .andExpect(jsonPath("$[2].peselNumber").value("90050556789"))
                .andExpect(jsonPath("$[2].height").value(175.0))
                .andExpect(jsonPath("$[2].weight").value(70.0))
                .andExpect(jsonPath("$[2].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[2].pensionValue").value(3200.00))
                .andExpect(jsonPath("$[2].workYears").value(42));
    }

    @Test
    void shouldReturnsPensionersWorkYearsTo35() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&workYearsTo=35"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("91010112345"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2500.00))
                .andExpect(jsonPath("$[0].workYears").value(35));
    }

    @Test
    void shouldReturnsPensionersPensionValueFrom3000() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&pensionValueFrom=3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Michael"))
                .andExpect(jsonPath("$[0].lastName").value("Johnson"))
                .andExpect(jsonPath("$[0].peselNumber").value("95030334567"))
                .andExpect(jsonPath("$[0].height").value(190.0))
                .andExpect(jsonPath("$[0].weight").value(80.0))
                .andExpect(jsonPath("$[0].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(3000.00))
                .andExpect(jsonPath("$[0].workYears").value(45))
                .andExpect(jsonPath("$[1].firstName").value("David"))
                .andExpect(jsonPath("$[1].lastName").value("Brown"))
                .andExpect(jsonPath("$[1].peselNumber").value("90050556789"))
                .andExpect(jsonPath("$[1].height").value(175.0))
                .andExpect(jsonPath("$[1].weight").value(70.0))
                .andExpect(jsonPath("$[1].email").value("davidbrown@example.com"))
                .andExpect(jsonPath("$[1].pensionValue").value(3200.00))
                .andExpect(jsonPath("$[1].workYears").value(42));
    }

    @Test
    void shouldReturnsPensionersPensionValueTo3000() throws Exception {

        mockMvc.perform(get("/person/find?type=pensioner&pensionValueTo=3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("91010112345"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2500.00))
                .andExpect(jsonPath("$[0].workYears").value(35))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].peselNumber").value("88020223456"))
                .andExpect(jsonPath("$[1].height").value(165.0))
                .andExpect(jsonPath("$[1].weight").value(60.0))
                .andExpect(jsonPath("$[1].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[1].pensionValue").value(2800.00))
                .andExpect(jsonPath("$[1].workYears").value(40))
                .andExpect(jsonPath("$[2].firstName").value("Michael"))
                .andExpect(jsonPath("$[2].lastName").value("Johnson"))
                .andExpect(jsonPath("$[2].peselNumber").value("95030334567"))
                .andExpect(jsonPath("$[2].height").value(190.0))
                .andExpect(jsonPath("$[2].weight").value(80.0))
                .andExpect(jsonPath("$[2].email").value("michaeljohnson@example.com"))
                .andExpect(jsonPath("$[2].pensionValue").value(3000.00))
                .andExpect(jsonPath("$[2].workYears").value(45))
                .andExpect(jsonPath("$[3].firstName").value("Emily"))
                .andExpect(jsonPath("$[3].lastName").value("Williams"))
                .andExpect(jsonPath("$[3].peselNumber").value("92040445678"))
                .andExpect(jsonPath("$[3].height").value(170.0))
                .andExpect(jsonPath("$[3].weight").value(65.0))
                .andExpect(jsonPath("$[3].email").value("emilywilliams@example.com"))
                .andExpect(jsonPath("$[3].pensionValue").value(2700.00))
                .andExpect(jsonPath("$[3].workYears").value(38));
    }

    @Test
    @ClearContext
    void shouldAddStudent() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "example college",
                2,
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=student&peselNumber=81981298"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                .andExpect(jsonPath("$[0].lastName").value("Kowalski"))
                .andExpect(jsonPath("$[0].peselNumber").value("81981298"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("janekkowal@gmail.com"))
                .andExpect(jsonPath("$[0].college").value("example college"))
                .andExpect(jsonPath("$[0].academicYear").value(2))
                .andExpect(jsonPath("$[0].scholarship").value(3000.0));
    }

    @Test
    @ClearContext
    void shouldAddEmployee() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                LocalDate.of(2022, 12,22),
                "designer",
                BigDecimal.valueOf(3000),
                1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=employee&peselNumber=81981299"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                .andExpect(jsonPath("$[0].lastName").value("Kowalski"))
                .andExpect(jsonPath("$[0].peselNumber").value("81981299"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("janekkowal@gmail.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-12-22"))
                .andExpect(jsonPath("$[0].actualProfession").value("designer"))
                .andExpect(jsonPath("$[0].salary").value(3000.0))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(1));
    }

    @Test
    @ClearContext
    void shouldAddPensioner() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "81981297",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=pensioner&peselNumber=81981297"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                .andExpect(jsonPath("$[0].lastName").value("Kowalski"))
                .andExpect(jsonPath("$[0].peselNumber").value("81981297"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(90.0))
                .andExpect(jsonPath("$[0].email").value("janekkowal@gmail.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(1000.0))
                .andExpect(jsonPath("$[0].workYears").value(60));
    }

    @Test
    void shouldReturnForbidden() throws Exception {

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "81981297",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void missingFirstName_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "",
                "Kowalski",
                "81981297",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("firstName is mandatory"));
    }

    @Test
    void missingLastName_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "",
                "81981297",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("lastName is mandatory"));
    }

    @Test
    void missingPeselNumber_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("peselNumber is mandatory"));
    }

    @Test
    void invalidHeight_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                -1.0,
                90.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing height"));
    }


    @Test
    void invalidWeight_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                -1.0,
                "janekkowal@gmail.com",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing weight"));
    }

    @Test
    void missingEmail_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                90.0,
                "cdcsc",
                BigDecimal.valueOf(1000),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing email"));
    }

    @Test
    void missingPensionValue_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                90.0,
                "jankowal@gmail.com",
                BigDecimal.valueOf(-1),
                60
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing pensionValue"));
    }

    @Test
    void missingWorkYears_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        PensionerCommand pensionerCommand = new PensionerCommand(
                "Jan",
                "Kowalski",
                "23465768753",
                90.0,
                90.0,
                "jankowal@gmail.com",
                BigDecimal.valueOf(1000),
                -1
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing workYears"));
    }

    @Test
    void missingCollege_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "",
                2,
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("college is mandatory"));
    }

    @Test
    void missingAcademicYear_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "example college",
                -1,
                BigDecimal.valueOf(3000)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing academicYear"));
    }

    @Test
    void missingScholarship_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        StudentCommand studentCommand = new StudentCommand(
                "Jan",
                "Kowalski",
                "81981298",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                "example college",
                1,
                BigDecimal.valueOf(0)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing scholarship"));
    }

    @Test
    void missingWorkStartDate_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                null,
                "designer",
                BigDecimal.valueOf(3000),
                1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid date"));
    }

    @Test
    void missingActualProfession_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                LocalDate.of(2022,12,22),
                "",
                BigDecimal.valueOf(3000),
                1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("actualProfession is mandatory"));
    }

    @Test
    void missingSalary_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                LocalDate.of(2022,12,22),
                "designer",
                BigDecimal.valueOf(0),
                1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing salary"));
    }

    @Test
    void missingNumberOfProfessions_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        EmployeeCommand employeeCommand = new EmployeeCommand(
                "Jan",
                "Kowalski",
                "81981299",
                180.0,
                90.0,
                "janekkowal@gmail.com",
                LocalDate.of(2022,12,22),
                "designer",
                BigDecimal.valueOf(3000),
                -1
        );
        String json = objectMapper.writeValueAsString(employeeCommand);

        mockMvc.perform(post("/person")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("missing numberOfProfessions"));
    }

    @Test
    @ClearContext
    void shouldEditPensionersWorkYearsAndPensionValue() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        mockMvc.perform(get("/person/find?type=pensioner&peselNumber=91010112345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("91010112345"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2500.00))
                .andExpect(jsonPath("$[0].workYears").value(35));

        PensionerEditCommand pensionerCommand = new PensionerEditCommand(
                "John",
                "Doe",
                "91010112345",
                180.0,
                75.0,
                "johndoe@example.com",
                1,
                BigDecimal.valueOf(2000),
                70
        );
        String json = objectMapper.writeValueAsString(pensionerCommand);

        mockMvc.perform(patch("/person/91010112345")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=pensioner&peselNumber=91010112345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].peselNumber").value("91010112345"))
                .andExpect(jsonPath("$[0].height").value(180.0))
                .andExpect(jsonPath("$[0].weight").value(75.0))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].pensionValue").value(2000.00))
                .andExpect(jsonPath("$[0].workYears").value(70));

    }

    @Test
    @ClearContext
    void shouldEditStudentFirstNameAndCollege() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        mockMvc.perform(get("/person/find?type=student&peselNumber=73620954782"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Stanford University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00));


        StudentEditCommand studentCommand = new StudentEditCommand(
                "Josh",
                "Smith",
                "73620954782",
                165.0,
                60.0,
                "janesmith@example.com",
                1,
                "Miami University",
                4,
                BigDecimal.valueOf(1800)
        );
        String json = objectMapper.writeValueAsString(studentCommand);

        mockMvc.perform(patch("/person/73620954782")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=student&peselNumber=73620954782"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Josh"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("73620954782"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$[0].college").value("Miami University"))
                .andExpect(jsonPath("$[0].academicYear").value(4))
                .andExpect(jsonPath("$[0].scholarship").value(1800.00));

    }

    @Test
    @ClearContext
    void shouldEditEmployeeEmailAndActualProfession() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.ADMIN);
        String token = jwtService.generateToken(user);

        mockMvc.perform(get("/person/find?type=employee&peselNumber=81020223456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("alicesmith@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Designer"))
                .andExpect(jsonPath("$[0].salary").value(4500.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));



        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                "Alice",
                "Smith",
                "81020223456",
                165.0,
                60.0,
                "alicesmith2@example.com",
                1,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500),
                2
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(patch("/person/81020223456")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(get("/person/find?type=employee&peselNumber=81020223456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].peselNumber").value("81020223456"))
                .andExpect(jsonPath("$[0].height").value(165.0))
                .andExpect(jsonPath("$[0].weight").value(60.0))
                .andExpect(jsonPath("$[0].email").value("alicesmith2@example.com"))
                .andExpect(jsonPath("$[0].workStartDate").value("2022-02-01"))
                .andExpect(jsonPath("$[0].actualProfession").value("Call center"))
                .andExpect(jsonPath("$[0].salary").value(4500.00))
                .andExpect(jsonPath("$[0].numberOfProfessions").value(2))
                .andExpect(jsonPath("$[0].numberOfPositions").value(0));
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    void edit_shouldReturnForbidden() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);


        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                "Alice",
                "Smith",
                "81020223456",
                165.0,
                60.0,
                "alicesmith2@example.com",
                1,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500),
                2
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(patch("/person/81020223456")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void edit_badPeselNumber_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                "Alice",
                "Smith",
                "3424242444",
                165.0,
                60.0,
                "alicesmith2@example.com",
                1,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500),
                2
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(patch("/person/3424242444")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad pesel number"));
    }
    @Test
    void edit_badVersion_shouldReturnBadRequest() throws Exception {
        User user = new User(null, "admin", "qwerty", UserRole.EMPLOYEE);
        String token = jwtService.generateToken(user);

        EmployeeEditCommand employeeEditCommandCommand = new EmployeeEditCommand(
                "Alice",
                "Smith",
                "81020223456",
                165.0,
                60.0,
                "alicesmith2@example.com",
                2,
                LocalDate.of(2022,2,1),
                "Call center",
                BigDecimal.valueOf(4500),
                2
        );
        String json = objectMapper.writeValueAsString(employeeEditCommandCommand);

        mockMvc.perform(patch("/person/81020223456")
                        .header("Authorization", format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("invalid version"));
    }


}
