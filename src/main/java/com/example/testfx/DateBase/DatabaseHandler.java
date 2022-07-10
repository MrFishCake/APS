package com.example.testfx.DateBase;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends DbConnectCfg {
    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:postgresql://" + dbHost + ":"
                + dbPort + "/" +dbName;

        dbConnection = DriverManager.getConnection(connectionString,
                dbUser, dbPass);

        return dbConnection;
    }

    public void signUpUser(User user) {
        String insert = "INSERT INTO " + Consts.USERS_TABLE + "(" +
                Consts.USERS_LOGIN + "," + Consts.USERS_PASSWORD + ")" +
                "VALUES(?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());

            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] signUpUser ERROR!");
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Consts.USERS_TABLE + " WHERE " +
                Consts.USERS_LOGIN + "=? AND " + Consts.USERS_PASSWORD + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());

            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] getUser ERROR!");
        }

        return resSet;
    }
    public ResultSet getUsername(User user) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Consts.USERS_TABLE + " WHERE " +
                Consts.USERS_LOGIN + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());

            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] getUsername ERROR!");
        }
        return resSet;
    }
    // ANIMALS
    public void NewAnimal(Animal animal) {
        String insert = "INSERT INTO " + Consts.ANIMALS_TABLE + "(" +
                Consts.ANIMALS_OWNER + "," + Consts.ANIMALS_ID_OWN + "," +
                Consts.ANIMALS_NICKNAME + "," + Consts.ANIMALS_KIND + "," +
                Consts.ANIMALS_GENDER + "," + Consts.ANIMALS_DATE_OF_BIRTH + ")" +
                "VALUES(?,?,?,?,?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, Animal.owner);
            prSt.setInt(2, Animal.id_own);
            prSt.setString(3, animal.getNickname());
            prSt.setString(4, animal.getKind());
            prSt.setString(5, animal.getGender());
            prSt.setDate(6, (Date) animal.getDate_of_birth());

            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] NewAnimal ERROR!");
        }
    }

    public int findCountAnimals() {
        int id = 0;

        String select = "SELECT COUNT(*) FROM ANIMALS WHERE owner:: character varying='" + Animal.owner+"';";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select,ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet resSet = prSt.executeQuery();

            resSet.next();
            id = resSet.getInt(1);

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] findCountAnimals ERROR!");
        }
        return id;
    }
    public ArrayList<String> getAnimalInfo(int id) {
        ArrayList<String> animal_info = new ArrayList<>();

        String select = "SELECT * FROM ANIMALS WHERE id_own="+id+" AND owner='"+Animal.owner+"';";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select,ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ResultSet resSet = prSt.executeQuery();

            resSet.next();
            animal_info.add(resSet.getString(4));
            animal_info.add(resSet.getString(5));
            animal_info.add(resSet.getString(6));
            animal_info.add(resSet.getString(7));

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] getAnimalInfo ERROR!");
        }

        return animal_info;
    }
    public ResultSet getNameAnimal(Animal animal) {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Consts.ANIMALS_TABLE + " WHERE " + Consts.ANIMALS_OWNER + "='" +
                Animal.owner + "' AND " + Consts.ANIMALS_NICKNAME + "=?";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, animal.getNickname());

            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] getNameAnimal ERROR!");
        }
        return resSet;
    }

    public void update_id(int id1, int id2) {
        String select = "UPDATE " + Consts.ANIMALS_TABLE + " SET id_own=?" + " WHERE id_own=?" +
                " AND owner='" + Animal.owner + "';";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setInt(1, id1);
            prSt.setInt(2, id2);

            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] update_id ERROR!");
            e.printStackTrace();
        }
    }

    public void deleteAnimal(int id) {
        String select = "DELETE FROM " + Consts.ANIMALS_TABLE + " WHERE id_own=?" +
                " AND owner='" + Animal.owner + "';";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setInt(1, id);

            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] deleteAnimal ERROR!");
            e.printStackTrace();
        }
    }

    public void updateEditAnimal(int id, Animal animal) {
        String select = "UPDATE " + Consts.ANIMALS_TABLE + " SET " + Consts.ANIMALS_NICKNAME + "=?, " +
                Consts.ANIMALS_KIND + "=?, " + Consts.ANIMALS_GENDER + "=?, " + Consts.ANIMALS_DATE_OF_BIRTH +
                "=? WHERE "+ Consts.ANIMALS_ID_OWN + "=" + id + " AND " + Consts.ANIMALS_OWNER +
                "='" + Animal.owner + "';";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, animal.getNickname());
            prSt.setString(2, animal.getKind());
            prSt.setString(3, animal.getGender());
            prSt.setDate(4, (Date) animal.getDate_of_birth());

            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("[DatabaseHandler class] updateEditAnimal ERROR!");
            e.printStackTrace();
        }
    }
}
