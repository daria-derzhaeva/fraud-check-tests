package database;

import database.dao.AccountDao;
import database.dao.CustomerDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSteps {

    public CustomerDao getCustomerByUsername(String username) {
        String query = """
                SELECT id, username, password, name, role
                FROM customers
                WHERE username = ?
                """;

        try (
                Connection connection = DatabaseClient.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("Customer was not found by username: " + username);
            }

            CustomerDao customer = new CustomerDao();
            customer.setId(resultSet.getLong("id"));
            customer.setUsername(resultSet.getString("username"));
            customer.setPassword(resultSet.getString("password"));
            customer.setName(resultSet.getString("name"));
            customer.setRole(resultSet.getString("role"));

            return customer;
        } catch (SQLException exception) {
            throw new RuntimeException("Cannot get customer by username: " + username, exception);
        }
    }

    public AccountDao getAccountById(long accountId) {
        String query = """
                SELECT id, account_number, balance, customer_id
                FROM accounts
                WHERE id = ?
                """;

        try (
                Connection connection = DatabaseClient.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, accountId);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("Account was not found by id: " + accountId);
            }

            AccountDao account = new AccountDao();
            account.setId(resultSet.getLong("id"));
            account.setAccountNumber(resultSet.getString("account_number"));
            account.setBalance(resultSet.getDouble("balance"));
            account.setCustomerId(resultSet.getLong("customer_id"));

            return account;
        } catch (SQLException exception) {
            throw new RuntimeException("Cannot get account by id: " + accountId, exception);
        }
    }
    public boolean accountExistsById(long accountId) {
        String query = """
            SELECT COUNT(*)
            FROM accounts
            WHERE id = ?
            """;

        try (
                Connection connection = DatabaseClient.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, accountId);

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return resultSet.getInt(1) > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Cannot check account existence by id: " + accountId, exception);
        }
    }
}