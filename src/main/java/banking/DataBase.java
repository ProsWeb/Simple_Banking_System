package banking;

import java.sql.*;

public class DataBase {

    private static final String DB_PATH = "card.db";

    public void createTable() {

        String url = "jdbc:sqlite:" + DB_PATH;

        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT NOT NULL,\n"
                + " balance INTEGER DEFAULT 0\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createAccount(final String account,
                              final String pin) {

        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, account);
            statement.setString(2, pin);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getBalance(final String account) {

        int balance = -1;
        String sql = "SELECT balance FROM card WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement statement  = conn.prepareStatement(sql)) {
            statement.setString(1, account);
            ResultSet rs  = statement.executeQuery();
            balance = rs.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return balance;
    }

    public void addMoney(final String account, final int income) {

        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, income);
            statement.setString(2, account);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void subtractMoney(final String receiverAccount,
                              final int moneyToTransfer) {

        String sql = "UPDATE card SET balance = balance - ? WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, moneyToTransfer);
            statement.setString(2, receiverAccount);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAccount(final String account) {

        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, account);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean accountExists(final String card, final String pin) {

        int count = 0;
        String sql = "SELECT COUNT(number) AS count FROM card "
                + "WHERE number = ? AND pin = ?";

        try (Connection conn = this.connect();
             PreparedStatement statement  = conn.prepareStatement(sql)) {
            statement.setString(1, card);
            statement.setString(2, pin);
            ResultSet rs  = statement.executeQuery();
            count = rs.getInt("count");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return count == 1;
    }

    public boolean accountExists(final String card) {

        int count = 0;
        String sql = "SELECT COUNT(number) AS count FROM card "
                + "WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement statement  = conn.prepareStatement(sql)) {
            statement.setString(1, card);
            ResultSet rs  = statement.executeQuery();
            count = rs.getInt("count");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return count == 1;
    }

    private Connection connect() {

        String url = "jdbc:sqlite:" + DB_PATH;
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
