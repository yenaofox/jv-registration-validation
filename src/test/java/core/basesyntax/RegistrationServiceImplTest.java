package core.basesyntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import core.basesyntax.service.RegistrationService;
import core.basesyntax.service.RegistrationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private final RegistrationService registrationService = new RegistrationServiceImpl();

    @AfterEach
    void tearDown() {
        Storage.people.clear();
    }

    private User createUser(String login, String password, Integer age) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setAge(age);
        return user;
    }

    @Test
    void register_validUser_ok() {
        User user = createUser("validLogin", "validPassword", 18);
        User actual = registrationService.register(user);
        assertEquals(user, actual);
        assertEquals(1, Storage.people.size());
        assertEquals(user, Storage.people.get(0));
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class,
                () -> registrationService.register(null));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_nullLogin_notOk() {
        User user = createUser(null, "validPassword", 18);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_shortLogin_notOk() {
        User user = createUser("login", "validPassword", 18);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_minLengthLogin_ok() {
        User user = createUser("login1", "validPassword", 18);
        User actual = registrationService.register(user);
        assertEquals(user, actual);
        assertEquals(1, Storage.people.size());
        assertEquals(user, Storage.people.get(0));
    }

    @Test
    void register_existingLogin_notOk() {
        User firstUser = createUser("sameLogin", "password1", 20);
        User secondUser = createUser("sameLogin", "password2", 25);
        Storage.people.add(firstUser);
        assertThrows(RegistrationException.class,
                () -> registrationService.register(secondUser));
        assertEquals(1, Storage.people.size());
        assertEquals(firstUser, Storage.people.get(0));
    }

    @Test
    void register_nullPassword_notOk() {
        User user = createUser("validLogin", null, 18);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_emptyPassword_notOk() {
        User user = createUser("validLogin", "", 18);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_shortPassword_notOk() {
        User user = createUser("validLogin", "pass1", 18);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_threeCharPassword_notOk() {
        User user = createUser("validLogin", "pas", 18);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_minLengthPassword_ok() {
        User user = createUser("validLogin", "pass12", 18);
        User actual = registrationService.register(user);
        assertEquals(user, actual);
        assertEquals(1, Storage.people.size());
        assertEquals(user, Storage.people.get(0));
    }

    @Test
    void register_longPassword_ok() {
        User user = createUser("validLogin", "pass1265768543", 18);
        User actual = registrationService.register(user);
        assertEquals(user, actual);
        assertEquals(1, Storage.people.size());
        assertEquals(user, Storage.people.get(0));
    }

    @Test
    void register_nullAge_notOk() {
        User user = createUser("validLogin", "validPassword", null);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_underAgeUser_notOk() {
        User user = createUser("validLogin", "validPassword", 17);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_negativeAge_notOk() {
        User user = createUser("validLogin", "validPassword", -17);

        assertThrows(RegistrationException.class,
                () -> registrationService.register(user));
        assertEquals(0, Storage.people.size());
    }

    @Test
    void register_minAgeUser_ok() {
        User user = createUser("validLogin", "validPassword", 18);
        User actual = registrationService.register(user);
        assertEquals(user, actual);
        assertEquals(1, Storage.people.size());
        assertEquals(user, Storage.people.get(0));
    }

    @Test
    void register_over18Age_ok() {
        User user = createUser("validLogin", "validPassword", 55);
        User actual = registrationService.register(user);
        assertEquals(user, actual);
        assertEquals(1, Storage.people.size());
        assertEquals(user, Storage.people.get(0));
    }
}
