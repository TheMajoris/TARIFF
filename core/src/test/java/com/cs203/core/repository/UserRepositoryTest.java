package com.cs203.core.repository;

import com.cs203.core.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/tariff_db",
        "spring.datasource.username=admin",
        "spring.datasource.password=admin123",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.sql.init.mode=never"
})
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private UserEntity adminUser;
    private UserEntity regularUser1;
    private UserEntity regularUser2;
    private UserEntity gmailUser;

    @BeforeEach
    void setUp() {
        // Create test data
        adminUser = new UserEntity();
        adminUser.setUsername("admin123");
        adminUser.setEmail("admin@company.com");
        adminUser.setPasswordHash("hashedPassword1");
        adminUser.setIsAdmin(true);
        adminUser.setFirstName("John");
        adminUser.setLastName("Admin");

        regularUser1 = new UserEntity();
        regularUser1.setUsername("john_doe");
        regularUser1.setEmail("john.doe@gmail.com");
        regularUser1.setPasswordHash("hashedPassword2");
        regularUser1.setIsAdmin(false);
        regularUser1.setFirstName("John");
        regularUser1.setLastName("Doe");

        regularUser2 = new UserEntity();
        regularUser2.setUsername("jane_smith");
        regularUser2.setEmail("jane.smith@company.com");
        regularUser2.setPasswordHash("hashedPassword3");
        regularUser2.setIsAdmin(false);
        regularUser2.setFirstName("Jane");
        regularUser2.setLastName("Smith");

        gmailUser = new UserEntity();
        gmailUser.setUsername("test_user");
        gmailUser.setEmail("test@gmail.com");
        gmailUser.setPasswordHash("hashedPassword4");
        gmailUser.setIsAdmin(false);
        gmailUser.setFirstName("Test");
        gmailUser.setLastName("User");

        // Persist test data
        entityManager.persistAndFlush(adminUser);
        entityManager.persistAndFlush(regularUser1);
        entityManager.persistAndFlush(regularUser2);
        entityManager.persistAndFlush(gmailUser);
    }

    @Test
    void testFindByUsername_Found() {
        Optional<UserEntity> result = userRepository.findByUsername("admin123");

        assertTrue(result.isPresent());
        assertEquals("admin123", result.get().getUsername());
        assertEquals("admin@company.com", result.get().getEmail());
        assertTrue(result.get().getIsAdmin());
    }

    @Test
    void testFindByUsername_NotFound() {
        Optional<UserEntity> result = userRepository.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByEmail_Found() {
        Optional<UserEntity> result = userRepository.findByEmail("john.doe@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("john_doe", result.get().getUsername());
        assertEquals("john.doe@gmail.com", result.get().getEmail());
        assertFalse(result.get().getIsAdmin());
    }

    @Test
    void testFindByEmail_NotFound() {
        Optional<UserEntity> result = userRepository.findByEmail("nonexistent@email.com");

        assertFalse(result.isPresent());
    }

    @Test
    void testExistsByUsername_True() {
        boolean exists = userRepository.existsByUsername("admin123");

        assertTrue(exists);
    }

    @Test
    void testExistsByUsername_False() {
        boolean exists = userRepository.existsByUsername("nonexistent");

        assertFalse(exists);
    }

    @Test
    void testExistsByEmail_True() {
        boolean exists = userRepository.existsByEmail("jane.smith@company.com");

        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_False() {
        boolean exists = userRepository.existsByEmail("nonexistent@email.com");

        assertFalse(exists);
    }

    @Test
    void testFindByIsAdminTrue() {
        List<UserEntity> adminUsers = userRepository.findByIsAdminTrue();

        assertEquals(1, adminUsers.size());
        assertEquals("admin123", adminUsers.get(0).getUsername());
        assertTrue(adminUsers.get(0).getIsAdmin());
    }

    @Test
    void testFindByIsAdminFalse() {
        List<UserEntity> regularUsers = userRepository.findByIsAdminFalse();

        assertEquals(3, regularUsers.size());
        assertThat(regularUsers)
                .extracting(UserEntity::getUsername)
                .containsExactlyInAnyOrder("john_doe", "jane_smith", "test_user");

        // Verify all are non-admin
        regularUsers.forEach(user -> assertFalse(user.getIsAdmin()));
    }

    @Test
    void testFindByFirstNameIgnoreCase() {
        List<UserEntity> johnUsers = userRepository.findByFirstNameIgnoreCase("john");

        assertEquals(2, johnUsers.size());
        assertThat(johnUsers)
                .extracting(UserEntity::getFirstName)
                .containsOnly("John");
    }

    @Test
    void testFindByFirstNameIgnoreCase_EmptyResult() {
        List<UserEntity> result = userRepository.findByFirstNameIgnoreCase("NonExistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByLastNameIgnoreCase() {
        List<UserEntity> doeUsers = userRepository.findByLastNameIgnoreCase("DOE");

        assertEquals(1, doeUsers.size());
        assertEquals("john_doe", doeUsers.get(0).getUsername());
        assertEquals("Doe", doeUsers.get(0).getLastName());
    }

    @Test
    void testFindByLastNameIgnoreCase_EmptyResult() {
        List<UserEntity> result = userRepository.findByLastNameIgnoreCase("NonExistent");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByUsernameContainingIgnoreCase() {
        List<UserEntity> usersWithJohn = userRepository.findByUsernameContainingIgnoreCase("JOHN");

        assertEquals(1, usersWithJohn.size());
        assertEquals("john_doe", usersWithJohn.get(0).getUsername());
    }

    @Test
    void testFindByUsernameContainingIgnoreCase_MultipleResults() {
        List<UserEntity> usersWithUnderscore = userRepository.findByUsernameContainingIgnoreCase("_");

        assertEquals(3, usersWithUnderscore.size());
        assertThat(usersWithUnderscore)
                .extracting(UserEntity::getUsername)
                .containsExactlyInAnyOrder("john_doe", "jane_smith", "test_user");
    }

    @Test
    void testFindByUsernameContainingIgnoreCase_EmptyResult() {
        List<UserEntity> result = userRepository.findByUsernameContainingIgnoreCase("xyz");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByEmailDomain_Gmail() {
        List<UserEntity> gmailUsers = userRepository.findByEmailDomain("gmail.com");

        assertEquals(2, gmailUsers.size());
        assertThat(gmailUsers)
                .extracting(UserEntity::getEmail)
                .allMatch(email -> email.contains("gmail.com"));
    }

    @Test
    void testFindByEmailDomain_Company() {
        List<UserEntity> companyUsers = userRepository.findByEmailDomain("company.com");

        assertEquals(2, companyUsers.size());
        assertThat(companyUsers)
                .extracting(UserEntity::getEmail)
                .allMatch(email -> email.contains("company.com"));
    }

    @Test
    void testFindByEmailDomain_EmptyResult() {
        List<UserEntity> result = userRepository.findByEmailDomain("nonexistent.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByFullNameContaining_FirstName() {
        List<UserEntity> johnUsers = userRepository.findByFullNameContaining("John");

        assertEquals(2, johnUsers.size());
        assertThat(johnUsers)
                .extracting(UserEntity::getFirstName)
                .containsOnly("John");
    }

    @Test
    void testFindByFullNameContaining_LastName() {
        List<UserEntity> smithUsers = userRepository.findByFullNameContaining("Smith");

        assertEquals(1, smithUsers.size());
        assertEquals("jane_smith", smithUsers.get(0).getUsername());
    }

    @Test
    void testFindByFullNameContaining_FullName() {
        List<UserEntity> johnDoeUsers = userRepository.findByFullNameContaining("John Doe");

        assertEquals(1, johnDoeUsers.size());
        assertEquals("john_doe", johnDoeUsers.get(0).getUsername());
    }

    @Test
    void testFindByFullNameContaining_PartialMatch() {
        List<UserEntity> usersWithAd = userRepository.findByFullNameContaining("Ad");

        assertEquals(1, usersWithAd.size());
        assertEquals("admin123", usersWithAd.get(0).getUsername());
    }

    @Test
    void testFindByFullNameContaining_EmptyResult() {
        List<UserEntity> result = userRepository.findByFullNameContaining("NonExistent Name");

        assertTrue(result.isEmpty());
    }

    @Test
    void testCountByIsAdminTrue() {
        long adminCount = userRepository.countByIsAdminTrue();

        assertEquals(1, adminCount);
    }

    @Test
    void testCountByIsAdminFalse() {
        long regularUserCount = userRepository.countByIsAdminFalse();

        assertEquals(3, regularUserCount);
    }

    @Test
    void testSaveAndFindUser() {
        UserEntity newUser = new UserEntity();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@test.com");
        newUser.setPasswordHash("hashedPassword");
        newUser.setIsAdmin(false);
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setEnabled(true);

        UserEntity savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId());

        Optional<UserEntity> foundUser = userRepository.findByUsername("newuser");
        assertTrue(foundUser.isPresent());
        assertEquals("newuser@test.com", foundUser.get().getEmail());
    }

    @Test
    void testDeleteUser() {
        Long userId = regularUser1.getId();
        assertTrue(userRepository.existsById(userId));

        userRepository.deleteById(userId);

        assertFalse(userRepository.existsById(userId));
        assertFalse(userRepository.findByUsername("john_doe").isPresent());
    }

    @Test
    void testUpdateUser() {
        UserEntity userToUpdate = userRepository.findByUsername("jane_smith").get();
        userToUpdate.setFirstName("Janet");
        userToUpdate.setEmail("janet.smith@company.com");

        UserEntity updatedUser = userRepository.save(userToUpdate);

        assertEquals("Janet", updatedUser.getFirstName());
        assertEquals("janet.smith@company.com", updatedUser.getEmail());

        // Verify the change persisted
        UserEntity reloadedUser = userRepository.findByUsername("jane_smith").get();
        assertEquals("Janet", reloadedUser.getFirstName());
        assertEquals("janet.smith@company.com", reloadedUser.getEmail());
    }

    @Test
    void testFindAll() {
        List<UserEntity> allUsers = userRepository.findAll();

        assertEquals(4, allUsers.size());
        assertThat(allUsers)
                .extracting(UserEntity::getUsername)
                .containsExactlyInAnyOrder("admin123", "john_doe", "jane_smith", "test_user");
    }
}