package dk.dtu.compute.se.pisd.roborally.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

 class SubRepository {
    Repository repo;
    Connector connector;
    public SubRepository(Repository repo, Connector connector) {
        this.repo = repo;
        this.connector = connector;
    }







    private static final String SQL_CREATE_COMMAND_FIELD =
            "INSERT INTO CommandCardField(gameID, playerID, card1," +
                    " card2, card3, card4, card5, card6, card7, card8)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement insert_player_command_field = null;

    PreparedStatement getInsertPlayerCommandField() {
        if (insert_player_command_field == null) {
            Connection connection = connector.getConnection();
            try {
                insert_player_command_field =
                        connection.prepareStatement(SQL_CREATE_COMMAND_FIELD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return insert_player_command_field;
    }

    private static final String SQL_SELECT_COMMAND_CARD_FIELD =
            "SELECT * FROM CommandCardField WHERE gameID = ? ORDER BY playerID ASC";
    private PreparedStatement select_command_card_field = null;
    protected PreparedStatement getCommandCardFieldStatement(){
        if (select_command_card_field == null) {
            Connection connection = connector.getConnection();
            try {
                select_command_card_field =
                        connection.prepareStatement(SQL_SELECT_COMMAND_CARD_FIELD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return select_command_card_field;
    }

    private static final String SQL_SELECT_PLAYER_REGISTER =
            "SELECT * FROM RegisterField WHERE gameID = ? ";

    private PreparedStatement select_player_register_stmt = null;

    private PreparedStatement getSelectRegisterFieldStatementU() {
        if (select_player_register_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_player_register_stmt =
                        connection.prepareStatement(SQL_SELECT_PLAYER_REGISTER,
                                ResultSet.TYPE_FORWARD_ONLY,
                                ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                // TODO error handling
                e.printStackTrace();
            }
        }
        return select_player_register_stmt;
    }


}
