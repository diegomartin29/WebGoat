package org.owasp.webgoat.lessons.challenges.challenge5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Assignment5 extends AssignmentEndpoint {

    private final LessonDataSource dataSource;
    private final Flags flags;

    @Autowired
    Connection connection;

    // Endpoint existente de Challenge 5
    @PostMapping("/challenge/5")
    @ResponseBody
    public AttackResult login(
            @RequestParam String username_login,
            @RequestParam String password_login) throws Exception {

        if (!StringUtils.hasText(username_login) || !StringUtils.hasText(password_login)) {
            return failed(this).feedback("required4").build();
        }
        if (!"Larry".equals(username_login)) {
            return failed(this).feedback("user.not.larry").feedbackArgs(username_login).build();
        }

        // Consulta parametrizada para evitar SQL Injection
        String query = "SELECT password FROM challenge_users WHERE userid = ? AND password = ?";
        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)) {
            statement.setString(1, username_login);
            statement.setString(2, password_login);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return success(this).feedback("challenge.solved").feedbackArgs(flags.getFlag(5)).build();
            } else {
                return failed(this).feedback("challenge.close").build();
            }
        }
    }

    // Nuevo Endpoint de Autenticación
    @GetMapping(value = "/authenticate")
    @ResponseBody
    public ResponseEntity<String> authenticate(
            @RequestParam("user") String user,
            @RequestParam("pass") String pass) throws SQLException {

        // Consulta parametrizada para evitar SQL Injection en el nuevo endpoint
        String query = "SELECT * FROM users WHERE user = ? AND pass = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user);
            statement.setString(2, pass);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error during authentication", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Authentication Success", HttpStatus.OK);
    }
}