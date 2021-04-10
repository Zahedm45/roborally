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

    private static final String SQL_SELECT_COMMAND_CARD_FIELD_UPDATE =
            "SELECT * FROM CommandCardField WHERE gameID = ? ";

    private PreparedStatement select_command_card_field_stmt = null;

    protected PreparedStatement getSelectCommandCardFieldStatementU() {
        if (select_command_card_field_stmt == null) {
            Connection connection = connector.getConnection();
            try {
                select_command_card_field_stmt =
                        connection.prepareStatement(
                                SQL_SELECT_COMMAND_CARD_FIELD_UPDATE,
                                ResultSet.TYPE_FORWARD_ONLY,
                                ResultSet.CONCUR_UPDATABLE);
            } catch (SQLException e) {
                // TODO error handling
                e.printStackTrace();
            }
        }
        return select_command_card_field_stmt;
    }





     private static final String SQL_DELETE_GAMES_ASC = "DELETE FROM Game  WHERE gameId = ?";

     private PreparedStatement delete_games_stm = null;

     protected PreparedStatement getDeleteGameU() {
         if (delete_games_stm == null) {
             Connection connection = connector.getConnection();
             try {
                 delete_games_stm = connection.prepareStatement(
                         SQL_DELETE_GAMES_ASC,
                         ResultSet.TYPE_FORWARD_ONLY,
                         ResultSet.CONCUR_UPDATABLE
                 );
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
         return delete_games_stm;
     }


     private static final String SQL_DELETE_PLAYER_ASC =
             "DELETE FROM Player  WHERE gameId = ?";

     private PreparedStatement delete_player_stm = null;

     protected PreparedStatement getDeletePlayerU() {
         if (delete_player_stm == null) {
             Connection connection = connector.getConnection();
             try {
                 delete_player_stm = connection.prepareStatement(
                         SQL_DELETE_PLAYER_ASC,
                         ResultSet.TYPE_FORWARD_ONLY,
                         ResultSet.CONCUR_UPDATABLE
                 );
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
         return delete_player_stm;
     }



     private static final String SQL_DELETE_REGISTER_FIELD_ASC =
             "DELETE FROM RegisterField WHERE gameId = ?";

     private PreparedStatement delete_register_field_stm = null;

     protected PreparedStatement getDeleteRegisterFieldU() {
         if (delete_register_field_stm == null) {
             Connection connection = connector.getConnection();
             try {
                 delete_register_field_stm = connection.prepareStatement(
                         SQL_DELETE_REGISTER_FIELD_ASC,
                         ResultSet.TYPE_FORWARD_ONLY,
                         ResultSet.CONCUR_UPDATABLE
                 );
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
         return delete_register_field_stm;
     }



     private static final String SQL_DELETE_COMMAND_CARD_FIELD_ASC =
             "DELETE FROM CommandCardField WHERE gameId = ?";

     private PreparedStatement delete_command_card_field_stm = null;

     protected PreparedStatement getDeleteCommandCardFieldU() {
         if (delete_command_card_field_stm == null) {
             Connection connection = connector.getConnection();
             try {
                 delete_command_card_field_stm = connection.prepareStatement(
                         SQL_DELETE_COMMAND_CARD_FIELD_ASC,
                         ResultSet.TYPE_FORWARD_ONLY,
                         ResultSet.CONCUR_UPDATABLE
                 );
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
         return delete_command_card_field_stm;
     }



 }
