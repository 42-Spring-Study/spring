package me.staek.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - ConnectionParam
 *
 * 커넥션 주입 로직 생성
 * - 해당 로직은 커넥션 리소스를 종료하지 않음.
 */
@Slf4j
public class BankAccountRepositoryV2 {

    private final DataSource dataSource;

    public BankAccountRepositoryV2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }

    public BankAccount save(BankAccount account) throws SQLException {
        String sql = "insert into account(account_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, account.getAccountId());
            pstmt.setInt(2, account.getMoney());
            pstmt.executeUpdate();
            return account;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }

    public BankAccount findById(String accountId) throws SQLException {
        String sql = "select * from account where account_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, accountId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                BankAccount member = new BankAccount();
                member.setAccountId(rs.getString("account_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("account not found accountId=" + accountId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }

    }

    public BankAccount findById(Connection con, String memberId) throws SQLException {
        String sql = "select * from account where account_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                BankAccount account = new BankAccount();
                account.setAccountId(rs.getString("account_id"));
                account.setMoney(rs.getInt("money"));
                return account;
            } else {
                throw new NoSuchElementException("account not found memberId=" + memberId);
            }

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //connection은 여기서 닫지 않는다.
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }

    }

    public void update(String accountId, int money) throws SQLException {
        String sql = "update account set money=? where account_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, accountId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }

    public void update(Connection con, String accountId, int money) throws SQLException {
        String sql = "update account set money=? where account_id=?";

        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, accountId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //connection은 여기서 닫지 않는다.
            JdbcUtils.closeStatement(pstmt);
        }

    }

    public void delete(String accountId) throws SQLException {
        String sql = "delete from account where account_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, accountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }

    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }
}
