package ua.edu.ukma.fin.userinfo.[:main-l-sd:];

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ua.edu.ukma.fin.userinfo.utils.UtilsForTests.getRandomEmail;
import static ua.edu.ukma.fin.userinfo.utils.UtilsForTests.getRandom[:main-h-ns:]View;

import ua.edu.ukma.fin.userinfo.criteria.[:main-h-ns:]Criteria;
import ua.edu.ukma.fin.userinfo.domain.entity.[:main-h-ns:]Entity;
import ua.edu.ukma.fin.userinfo.domain.enums.PermissionsEnum;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]ListResponse;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]Response;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]View;
import ua.edu.ukma.fin.userinfo.utils.ApiUrls;
import ua.edu.ukma.fin.userinfo.utils.BaseCRUDMethods;
import ua.edu.ukma.fin.userinfo.utils.BaseTest;
import ua.edu.ukma.fin.userinfo.utils.UtilsForTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

public class [:main-h-ns:]CRUDTest extends BaseTest {

    private final BaseCRUDMethods<[:main-h-ns:]View, Integer> baseCRUDMethods;

    private final String canDoEverythingEmail = getRandomEmail();
    private final String canDoNothingEmail = getRandomEmail();


    @Autowired
    public [:main-h-ns:]CRUDTest(UtilsForTests utilsForTests) {
        baseCRUDMethods = new BaseCRUDMethods<>(ApiUrls.[:main-u-su:]_API, utilsForTests);
    }

    @BeforeEach
    public void init() {
        mockPermissions();
        initEntities();
    }

    private void mockPermissions() {
        mockUserPermissions(canDoEverythingEmail, PermissionsEnum.VIEW_[:main-u-su:], PermissionsEnum.CREATE_[:main-u-su:], PermissionsEnum.EDIT_[:main-u-su:], PermissionsEnum.DELETE_[:main-u-su:]);
        mockUserPermissions(canDoNothingEmail);
        mockUserPermissions(TEACHER_EMAIL);
        mockUserPermissions(STUDENT_EMAIL);
        mockUserPermissions(FACULTY_STAFF_EMAIL);
        mockUserPermissions(EMPLOYEE_EMAIL);
    }

    private void initEntities() {
        Integer facultyId = createFaculty();
        Integer contactPersonId = createContactPerson();
        Integer buddyId = createContactPerson();
        List<Integer> languages = createLanguages(2);
        Integer epId = createEducationProgram(1, facultyId,
                contactPersonId, buddyId, languages);
        Integer currentYearOfStudyingId = createYearOfStudying(LocalDate.now().getYear(), LocalDate.now().getYear()+1, true);
        Integer curriculumId = createCurriculum(epId, currentYearOfStudyingId);
        Integer departmentId = createDepartment(facultyId);

        createStudent(facultyId, 4, curriculumId, STUDENT_EMAIL);
        createTeacher(departmentId, TEACHER_EMAIL);
        createFacultyStaff(facultyId, FACULTY_STAFF_EMAIL);
        createFacultyStaff(facultyId, FACULTY_STAFF_OTHER_EMAIL);
        createEmployee(EMPLOYEE_EMAIL);
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canCreateEntityForUser_whenHavePermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        assertTrue([:main-rs-ns:]Repository.findById(id).isPresent());
        assertTrue(checkIfEntityMatchesView(id, view));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void cannotCreateEntityForUser_whenHaveNoPermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        baseCRUDMethods.createRequestFail(view, canDoNothingEmail, HttpServletResponse.SC_FORBIDDEN);

        assertEquals(0, [:main-rs-ns:]Repository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canCreateEntityForThemself(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, user);

        assertTrue([:main-rs-ns:]Repository.findById(id).isPresent());
        assertTrue(checkIfEntityMatchesView(id, view));
    }

    @Test
    public void cannotCreateInvalidEntity() {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View("");
        baseCRUDMethods.createRequestFail(view, canDoEverythingEmail, HttpServletResponse.SC_NOT_FOUND);
        assertEquals(0, [:main-rs-ns:]Repository.count());

        view = getRandom[:main-h-ns:]View();
        view.set();
        baseCRUDMethods.createRequestFail(view, canDoEverythingEmail, HttpServletResponse.SC_CONFLICT);
        assertEquals(0, [:main-rs-ns:]Repository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canUpdateEntityForUser_whenHavePermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        [:main-h-ns:]View newView = getRandom[:main-h-ns:]View(id, user);
        boolean isUpdated = baseCRUDMethods.updateRequest(newView, canDoEverythingEmail);

        assertTrue(isUpdated);
        assertTrue(checkIfEntityMatchesView(id, newView));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canUpdateEntityForThemself(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        [:main-h-ns:]View newView = getRandom[:main-h-ns:]View(id, user);
        boolean isUpdated = baseCRUDMethods.updateRequest(newView, user);

        assertTrue(isUpdated);
        assertTrue(checkIfEntityMatchesView(id, newView));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void cannotUpdateEntityForUser_whenHaveNoPermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        [:main-h-ns:]View newView = getRandom[:main-h-ns:]View(id, user);
        baseCRUDMethods.updateRequestFail(newView, canDoNothingEmail, HttpServletResponse.SC_FORBIDDEN);

        assertTrue(checkIfEntityMatchesView(id, view));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canDeleteEntityForUser_whenHavePermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        baseCRUDMethods.delete(id , canDoEverythingEmail);

        assertEquals(0, [:main-rs-ns:]Repository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void cannotDeleteEntityForUser_whenHaveNoPermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        baseCRUDMethods.deleteFail(id , canDoNothingEmail, HttpServletResponse.SC_FORBIDDEN);

        assertEquals(1, [:main-rs-ns:]Repository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canDeleteEntityForThemself(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        baseCRUDMethods.delete(id , user);

        assertEquals(0, [:main-rs-ns:]Repository.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canViewEntityForUser_whenHavePermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        [:main-h-ns:]Response response = baseCRUDMethods.getInfoAsObject(id, canDoEverythingEmail, [:main-h-ns:]Response.class);

        assertTrue(checkIfResponseMatchesEntity(response, id));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void cannotViewEntityForUser_whenHaveNoPermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        baseCRUDMethods.getRequestFail("/" + id, canDoNothingEmail, Map.of(), HttpServletResponse.SC_FORBIDDEN);
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canViewEntityForThemself(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        Integer id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        [:main-h-ns:]Response response = baseCRUDMethods.getInfoAsObject(id, user, [:main-h-ns:]Response.class);

        assertTrue(checkIfResponseMatchesEntity(response, id));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canViewEntitiesByEmail_whenHavePermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        int id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        [:main-h-ns:]ListResponse response = baseCRUDMethods.getAsObject("/user/" + user, canDoEverythingEmail, [:main-h-ns:]ListResponse.class);

        assertEquals(1, response.getTotal());
        assertTrue(checkIfResponseMatchesEntity(response.getItems().get(0), id));
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void cannotViewEntitiesByEmail_whenHaveNoPermission(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        baseCRUDMethods.getRequestFail("/user/" + user, canDoNothingEmail, Map.of(), HttpServletResponse.SC_FORBIDDEN);
    }

    @ParameterizedTest
    @ValueSource(strings = {TEACHER_EMAIL, FACULTY_STAFF_EMAIL, EMPLOYEE_EMAIL})
    public void canViewEntitiesByEmailForThemself(String user) {
        [:main-h-ns:]View view = getRandom[:main-h-ns:]View(user);
        int id = baseCRUDMethods.createRequest(view, canDoEverythingEmail);

        [:main-h-ns:]ListResponse response = baseCRUDMethods.getAsObject("/user/" + user, user, [:main-h-ns:]ListResponse.class);

        assertEquals(1, response.getTotal());
        assertTrue(checkIfResponseMatchesEntity(response.getItems().get(0), id));
    }

    @Test
    public void canViewEntitiesByCriteria_whenHavePermission() {
        List<Integer> ids = new ArrayList<>();
        getViewsForCriteriaTest().forEach((view) -> ids.add(baseCRUDMethods.createRequest(view, canDoEverythingEmail)));

        [:main-h-ns:]Criteria criteria = new [:main-h-ns:]Criteria();
        criteria.setIds(ids.subList(0, 2));
        assertEquals(2, getListResponseByCriteria(criteria).getItems().size());

        criteria = new [:main-h-ns:]Criteria();
        criteria.set();
        assertEquals(, getListResponseByCriteria(criteria).getItems().size());
    }

    private List<[:main-h-ns:]View> getViewsForCriteriaTest() {
        List<[:main-h-ns:]View> result = new ArrayList<>();

        [:main-h-ns:]View view = getRandom[:main-h-ns:]View();
        view.set();
        result.add(view);

        return result;
    }

    private [:main-h-ns:]ListResponse getListResponseByCriteria([:main-h-ns:]Criteria criteria) {
        return baseCRUDMethods.getListResponseByCriteria(canDoEverythingEmail, criteria, [:main-h-ns:]ListResponse.class);
    }

    private boolean checkIfEntityMatchesView(Integer entityId, [:main-h-ns:]View view) {
        [:main-h-ns:]Entity entity = [:main-rs-ns:]Repository.findById(entityId).orElseThrow();
        boolean isEqual = true;
        if (view.getUserEmail() != null)
            isEqual &= Objects.equals(entity.getUser().getEmail(), view.getUserEmail());
        if (view.get() != null)
            isEqual &= ;
        return isEqual;
    }

    private boolean checkIfResponseMatchesEntity([:main-h-ns:]Response response, Integer entityId) {
        [:main-h-ns:]Entity entity = [:main-rs-ns:]Repository.findById(entityId).orElseThrow();
        boolean isEqual = true;
        isEqual &= Objects.equals(response.getUserEmail(), entity.getUser().getEmail());
        isEqual &= ;

        return isEqual;
    }
}
