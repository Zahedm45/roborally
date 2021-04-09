/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.dal;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;

import java.sql.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
class Repository implements IRepository {

	private static final String GAME_GAMEID = "gameID";

	private static final String GAME_NAME = "name";

	private static final String GAME_CURRENTPLAYER = "currentPlayer";

	private static final String GAME_PHASE = "phase";

	private static final String GAME_STEP = "step";

	private static final String PLAYER_PLAYERID = "playerID";

	private static final String PLAYER_NAME = "name";

	private static final String PLAYER_COLOUR = "colour";

	private static final String PLAYER_GAMEID = "gameID";

	private static final String PLAYER_POSITION_X = "positionX";

	private static final String PLAYER_POSITION_Y = "positionY";

	private static final String PLAYER_HEADING = "heading";

	private static final String CARD = "card";

	private Connector connector;
	private SubRepository subRepository;

	public Repository(Connector connector){
		this.connector = connector;
		this.subRepository = new SubRepository(this, connector);
	}





	@Override
	public boolean createGameInDB(Board game) {
		if (game.getGameId() == null) {
			Connection connection = connector.getConnection();
			try {
				connection.setAutoCommit(false);

				PreparedStatement ps = getInsertGameStatementRGK();
				// TODO: the name should eventually set by the user
				//       for the game and should be then used
				//       game.getName();

				LocalDateTime localDateTime = LocalDateTime.now();
				DateTimeFormatter format = DateTimeFormatter.ofPattern(" dd-MM-yyyy HH:mm:ss");

				ps.setString(1, localDateTime.format(format)); // instead of name
				ps.setNull(2, Types.TINYINT); // game.getPlayerNumber(game.getCurrentPlayer())); is inserted after players!
				ps.setInt(3, game.getPhase().ordinal());
				ps.setInt(4, game.getStep());

				// If you have a foreign key constraint for current players,
				// the check would need to be temporarily disabled, since
				// MySQL does not have a per transaction validation, but
				// validates on a per row basis.
				// Statement statement = connection.createStatement();
				// statement.execute("SET foreign_key_checks = 0");

				int affectedRows = ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
				if (affectedRows == 1 && generatedKeys.next()) {
					game.setGameId(generatedKeys.getInt(1));
				}
				generatedKeys.close();

				// Enable foreign key constraint check again:
				// statement.execute("SET foreign_key_checks = 1");
				// statement.close();

				createPlayersInDB(game);
				/* TOODO this method needs to be implemented first
				createCardFieldsInDB(game);
				 */

				createCardFieldsInDB(game);

				// since current player is a foreign key, it can oly be
				// inserted after the players are created, since MySQL does
				// not have a per transaction validation, but validates on
				// a per row basis.
				ps = getSelectGameStatementU();
				ps.setInt(1, game.getGameId());

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
					rs.updateRow();
				} else {
					// TODO error handling
				}
				rs.close();

				connection.commit();
				connection.setAutoCommit(true);
				return true;
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
				System.err.println("Some DB error");

				try {
					connection.rollback();
					connection.setAutoCommit(true);
				} catch (SQLException e1) {
					// TODO error handling
					e1.printStackTrace();
				}
			}
		} else {
			System.err.println("Game cannot be created in DB, since it has a game id already!");
		}
		return false;
	}

	@Override
	public boolean updateGameInDB(Board game) {
		assert game.getGameId() != null;

		Connection connection = connector.getConnection();
		try {
			connection.setAutoCommit(false);

			PreparedStatement ps = getSelectGameStatementU();
			ps.setInt(1, game.getGameId());

			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern(" dd-MM-yyyy HH:mm:ss");

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				rs.updateString(GAME_NAME, localDateTime.format(format)); // instead of name
				rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
				rs.updateInt(GAME_PHASE, game.getPhase().ordinal());
				rs.updateInt(GAME_STEP, game.getStep());
				rs.updateRow();
			} else {
				// TODO error handling
			}

			rs.close();
			updatePlayersInDB(game);
			/* TOODO this method needs to be implemented first
			updateCardFieldsInDB(game);
			*/
			updateCardFieldsInDB(game);

			connection.commit();
			connection.setAutoCommit(true);


			return true;
		} catch (SQLException e) {
			// TODO error handling
			e.printStackTrace();
			System.err.println("Some DB error");

			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO error handling
				e1.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public Board loadGameFromDB(int id) {
		Board game;
		try {
			// TODO here, we could actually use a simpler statement
			//      which is not updatable, but reuse the one from
			//      above for the pupose
			PreparedStatement ps = getSelectGameStatementU();
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			int playerNo = -1;
			if (rs.next()) {
				// TODO the width and height could eventually come from the database
				// int width = AppController.BOARD_WIDTH;
				// int height = AppController.BOARD_HEIGHT;
				// game = new Board(width,height);
				// TODO and we should also store the used game board in the database
				//      for now, we use the default game board
				game = LoadBoard.loadBoard(null);
				if (game == null) {
					return null;
				}
				playerNo = rs.getInt(GAME_CURRENTPLAYER);
				// TODO currently we do not set the games name (needs to be added)
				game.setPhase(Phase.values()[rs.getInt(GAME_PHASE)]);
				game.setStep(rs.getInt(GAME_STEP));
			} else {
				// TODO error handling
				return null;
			}
			rs.close();

			game.setGameId(id);
			loadPlayersFromDB(game);

			if (playerNo >= 0 && playerNo < game.getPlayersNumber()) {
				game.setCurrentPlayer(game.getPlayer(playerNo));
			} else {
				// TODO  error handling
				return null;
			}

			/* TOODO this method needs to be implemented first
			loadCardFieldsFromDB(game);
			*/
			loadCardFieldsFromDB(game);



			return game;
		} catch (SQLException e) {
			// TODO error handling
			e.printStackTrace();
			System.err.println("Some DB error");
		}
		return null;
	}



	@Override
	public List<GameInDB> getGames() {
		// TODO when there many games in the DB, fetching all available games
		//      from the DB is a bit extreme; eventually there should a
		//      methods that can filter the returned games in order to
		//      reduce the number of the returned games.
		List<GameInDB> result = new ArrayList<>();
		try {
			PreparedStatement ps = getSelectGameIdsStatement();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(GAME_GAMEID);
				String name = rs.getString(GAME_NAME);
				result.add(new GameInDB(id,name));
			}
			rs.close();

		} catch (SQLException e) {
			// TODO proper error handling
			e.printStackTrace();
		}
		result.sort(Comparator.comparing(GameInDB:: getName).reversed());
		return result;

	}


//	public List<String> getGamesOrderedByDate() {
//		// TODO when there many games in the DB, fetching all available games
//		//      from the DB is a bit extreme; eventually there should a
//		//      methods that can filter the returned games in order to
//		//      reduce the number of the returned games.
//		List<LocalDateTime> dateList = new ArrayList<>();
//
//		//LocalDateTime localDateTime = LocalDateTime.now();
//		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//		try {
//			PreparedStatement ps = getSelectGameIdsStatement();
//			ResultSet rs = ps.executeQuery();
//			while (rs.next()) {
//				//int id = rs.getInt(GAME_GAMEID);
//				String name = rs.getString(GAME_NAME);
//				dateList.add(LocalDateTime.parse(name, format));
//			}
//			rs.close();
//
//		} catch (SQLException e) {
//			// TODO proper error handling
//			e.printStackTrace();
//		}
//
//		Collections.sort(dateList);
//
//		for (LocalDateTime d : dateList) {
//			System.out.println(d);
//
//		}
//		return null;
//
//	}


	private void createPlayersInDB(Board game) throws SQLException {
		// TODO code should be more defensive
		PreparedStatement ps = getSelectPlayersStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		for (int i = 0; i < game.getPlayersNumber(); i++) {
			Player player = game.getPlayer(i);
			rs.moveToInsertRow();
			rs.updateInt(PLAYER_GAMEID, game.getGameId());
			rs.updateInt(PLAYER_PLAYERID, i);
			rs.updateString(PLAYER_NAME, player.getName());
			rs.updateString(PLAYER_COLOUR, player.getColor());
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());

			rs.insertRow();
		}
		rs.close();
	}

	private void createCardFieldsInDB(Board game) throws SQLException {
		// TODO code should be more defensive
		PreparedStatement rs = insertPlayerRegisterField();
		for (int i = 0; i < game.getPlayersNumber(); i++) {
			Player player = game.getPlayer(i);
			rs.setInt(1, game.getGameId());
			rs.setInt(2, i);
			for (int j = 3; j < 8; j++) {
				CommandCard playerCard = player.getProgramField(j-3).getCard();
				if (playerCard != null){
					rs.setString(j, playerCard.getName());
				} else rs.setString(j, null);
			}
			rs.executeUpdate();

			PreparedStatement commandField = subRepository.getInsertPlayerCommandField();
			if (commandField != null) {
				commandField.setInt(1, game.getGameId());
				commandField.setInt(2, i);
				for (int k = 3; k < 11; k++) {
					CommandCard cardField = player.getCardField(k-3).getCard();
					if (cardField != null) {
						commandField.setString(k, cardField.getName());
					} else commandField.setString(k, null);

				}
				commandField.executeUpdate();
			}

		}
		rs.close();
	}

	private void updateCardFieldsInDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectRegisterFieldStatementU();
		ps.setInt(1, game.getGameId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			int playerID = rs.getInt(PLAYER_PLAYERID);
			for (int j = 0; j < 5; j++) {
				String strJ = String.valueOf(j + 1);
				CommandCard str =
						game.getPlayer(playerID).getProgramField((j)).getCard();
				if (str != null) {
					rs.updateString(CARD + strJ, str.getName());
				} else rs.updateString(CARD + strJ, null);
			}
			rs.updateRow();
		}


		PreparedStatement ps2 = subRepository.getSelectCommandCardFieldStatementU();
		ps2.setInt(1, game.getGameId());
		ResultSet rs2 = ps2.executeQuery();

		while (rs2.next()) {
			int playerID = rs2.getInt(PLAYER_PLAYERID);
			for (int k = 0; k < 8; k++) {
				String strJ = String.valueOf(k + 1);
				CommandCard str =
						game.getPlayer(playerID).getCardField((k)).getCard();
				if (str != null) {
					rs2.updateString(CARD + strJ, str.getName());
				} else rs2.updateString(CARD + strJ, null);
			}
			rs2.updateRow();
		}
		rs.close();
		rs2.close();

	}

	private void loadCardFieldsFromDB(Board game) throws SQLException {
		PreparedStatement ps = getRegisterFieldStatement();
		ps.setInt(1, game.getGameId());
		ResultSet rs = ps.executeQuery();
		int i = 0;
		while (rs.next()) {
			int playerID = rs.getInt(PLAYER_PLAYERID);
			if (i++ == playerID) {

				for (int j = 1; j < 6; j++) {
					String strJ = String.valueOf(j);
					String card = rs.getString(CARD + strJ);
					if (card != null) {
						Command command = Command.getCardByDisplayName(card);
						if (command != null) {
							CommandCard commandCard = new CommandCard(command);
							game.getPlayer(i-1).getProgramField((j-1) ).setCard(commandCard);
						}
					}
				}
			}
		}

		PreparedStatement ps2 = subRepository.getCommandCardFieldStatement();
		ps2.setInt(1, game.getGameId());
		ResultSet rs2 = ps2.executeQuery();


		int counter = 0;
		while (rs2.next()) {
			int playerID = rs2.getInt(PLAYER_PLAYERID);

			if (counter++ == playerID) {
				for (int k = 1; k < 9; k++) {
					String strK = String.valueOf(k);
					String cardName = rs2.getString(CARD + strK);
					if (cardName != null) {
						Command command = Command.getCardByDisplayName(cardName);
						if (command != null) {
							CommandCard commandCard = new CommandCard(command);
							game.getPlayer(counter-1).getCardField((k-1) ).setCard(commandCard);
						}
					}
				}
			}
		}
		rs.close();
		rs2.close();
	}

	private void loadPlayersFromDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectPlayersASCStatement();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		int i = 0;
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			if (i++ == playerId) {
				// TODO this should be more defensive
				String name = rs.getString(PLAYER_NAME);
				String colour = rs.getString(PLAYER_COLOUR);
				Player player = new Player(game, colour ,name);
				game.addPlayer(player);

				int x = rs.getInt(PLAYER_POSITION_X);
				int y = rs.getInt(PLAYER_POSITION_Y);
				player.setSpace(game.getSpace(x,y));
				int heading = rs.getInt(PLAYER_HEADING);
				player.setHeading(Heading.values()[heading]);

				// TODO  should also load players program and hand here
			} else {
				// TODO error handling
				System.err.println("Game in DB does not have a player with id " + i +"!");
			}
		}
		rs.close();
	}

	private void updatePlayersInDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectPlayersStatementU();
		ps.setInt(1, game.getGameId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			// TODO should be more defensive
			Player player = game.getPlayer(playerId);
			// rs.updateString(PLAYER_NAME, player.getName()); // not needed: player's names does not change
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			// TODO error handling
			// TODO take care of case when number of players changes, etc
			rs.updateRow();
		}
		rs.close();

		// TODO error handling/consistency check: check whether all players were updated
	}


	@Override
	public void deleteGameInDB(Board game) {
		PreparedStatement ps = subRepository.getDeleteCommandCardFieldU();





		try {
			System.out.println(game.getGameId());
			ps.setInt(1, game.getGameId());
			ps.executeUpdate();

			PreparedStatement ps1 = subRepository.getDeleteRegisterFieldU();
			ps1.setInt(1, game.getGameId());
			ps1.executeUpdate();


			PreparedStatement ps3 = subRepository.getDeleteGameU();
			ps3.setInt(1, game.getGameId());
			ps3.executeUpdate();

			PreparedStatement ps2 = subRepository.getDeletePlayerU();
			ps2.setInt(1, game.getGameId());
			ps2.executeUpdate();





//			ps1.executeUpdate();
//			ps2.executeUpdate();
//			ps3.executeUpdate();


		} catch (SQLException e) {
			e.printStackTrace();
		}

	}




	private static final String SQL_INSERT_GAME =
			"INSERT INTO Game(name, currentPlayer, phase, step) VALUES (?, ?, ?, ?)";

	private PreparedStatement insert_game_stmt = null;

	private PreparedStatement getInsertGameStatementRGK() {
		if (insert_game_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				insert_game_stmt = connection.prepareStatement(
						SQL_INSERT_GAME,
						Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return insert_game_stmt;
	}



	private static final String SQL_CREATE_REGISTER_FIELD =
			"INSERT INTO RegisterField(gameID, playerID, card1, card2, card3, card4, card5)" +
					" VALUES (?, ?, ?, ?, ?, ?, ?)";
	private PreparedStatement insert_register_field = null;

	private PreparedStatement insertPlayerRegisterField() {
		if (insert_register_field == null) {
			Connection connection = connector.getConnection();
			try {
				insert_register_field =
						connection.prepareStatement(SQL_CREATE_REGISTER_FIELD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return insert_register_field;
	}

	private static final String SQL_SELECT_REGISTER_FIELD =
			"SELECT * FROM RegisterField WHERE gameID = ? ORDER BY playerID ASC";
	private PreparedStatement select_register_field = null;
	private PreparedStatement getRegisterFieldStatement(){
		if (select_register_field == null) {
			Connection connection = connector.getConnection();
			try {
				select_register_field =
						connection.prepareStatement(SQL_SELECT_REGISTER_FIELD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return select_register_field;
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



	private static final String SQL_SELECT_GAME =
			"SELECT * FROM Game WHERE gameID = ?";

	private PreparedStatement select_game_stmt = null;

	private PreparedStatement getSelectGameStatementU() {
		if (select_game_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_game_stmt = connection.prepareStatement(
						SQL_SELECT_GAME,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_game_stmt;
	}

	private static final String SQL_SELECT_PLAYERS =
			"SELECT * FROM Player WHERE gameID = ?";

	private PreparedStatement select_players_stmt = null;

	private PreparedStatement getSelectPlayersStatementU() {
		if (select_players_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_players_stmt = connection.prepareStatement(
						SQL_SELECT_PLAYERS,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_players_stmt;
	}

	private static final String SQL_SELECT_PLAYERS_ASC =
			"SELECT * FROM Player WHERE gameID = ? ORDER BY playerID ASC";

	private PreparedStatement select_players_asc_stmt = null;

	private PreparedStatement getSelectPlayersASCStatement() {
		if (select_players_asc_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				// This statement does not need to be updatable
				select_players_asc_stmt = connection.prepareStatement(
						SQL_SELECT_PLAYERS_ASC);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_players_asc_stmt;
	}

	private static final String SQL_SELECT_GAMES =
			"SELECT gameID, name FROM Game";

	private PreparedStatement select_games_stmt = null;

	private PreparedStatement getSelectGameIdsStatement() {
		if (select_games_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_games_stmt = connection.prepareStatement(
						SQL_SELECT_GAMES);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_games_stmt;
	}






}
