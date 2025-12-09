package com.example.cvbuilder_2207094; // Change this to your actual package

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    public static class CVRecord {

        public int id;

        public String name;
        public String email;
        public String phone;
        public String address;
        public String workExperience;
        public String skills;
        public String projects;

        public String exam;
        public String institute;
        public String boardUniversity;
        public String session;
        public String result;
    }
    private static final String URL = "jdbc:sqlite:cv_builder.db";
    private static Connection connect() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(URL);
        return conn;
    }


    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS cv_records (\n"
                + " phone_number TEXT PRIMARY KEY,\n"
                + " name TEXT NOT NULL,\n"
                + " email TEXT,\n"
                + " address TEXT,\n"
                + " skills TEXT,\n"
                + " work_experience TEXT,\n"
                + " projects TEXT,\n"
                + " exam TEXT, \n"
                + " session TEXT, \n"
                + " result TEXT, \n"
                + " institute TEXT, \n"
                + " board_university TEXT\n"
                + ")";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("CV table created or already exists.");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    public CVRecord getCV(String phoneToSearch) {
        String sql = "SELECT * FROM cv_records WHERE phone = ?";
        CVRecord record = null;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phoneToSearch);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                record = new CVRecord();

                record.phone = rs.getString("phone");
                record.name = rs.getString("name");
                record.email = rs.getString("email");
                record.address = rs.getString("address");


                record.workExperience = rs.getString("work_experience");
                record.skills = rs.getString("skills");
                record.projects = rs.getString("projects");
                record.exam = rs.getString("exam");
                record.session = rs.getString("session");
                record.result = rs.getString("result");
                record.institute = rs.getString("institute");
                record.boardUniversity = rs.getString("board_university");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching CV: " + e.getMessage());
            e.printStackTrace();
        }

        return record;
    }

    public static boolean insertCV(CVRecord record) {
        String sql = "INSERT INTO cv_records(phone_number, name, email, address, skills, work_experience, projects, exam, session, result, institute, board_university) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, record.phone);
            pstmt.setString(2, record.name);
            pstmt.setString(3, record.email);
            pstmt.setString(4, record.address);
            pstmt.setString(5, record.skills);
            pstmt.setString(6, record.workExperience);
            pstmt.setString(7, record.projects);
            pstmt.setString(8, record.exam);
            pstmt.setString(9, record.session);
            pstmt.setString(10, record.result);
            pstmt.setString(11, record.institute);
            pstmt.setString(12, record.boardUniversity);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saving record: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all CV records for display in the table view.
     */
//    public static List<CVRecord> getAllCVRecords() {
//        String sql = "SELECT name, email, phone_number, address FROM cv_records";
//        List<CVRecord> records = new ArrayList<>();
//
//        try (Connection conn = connect();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                records.add(new CVRecord(
//                        rs.getString("name"),
//                        rs.getString("email"),
//                        rs.getString("phone_number"),
//                        rs.getString("address")
//                ));
//            }
//        } catch (SQLException e) {
//            System.err.println("Error retrieving records: " + e.getMessage());
//        }
//        return records;
//    }


    // Add this method inside your DatabaseHelper class
    public List<CVRecord> getAllCVs() {
        List<CVRecord> list = new ArrayList<>();
        String sql = "SELECT phone_number, name FROM cv_records";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CVRecord record = new CVRecord();
                // Map DB column "phone_number" to Object field "phone"
                record.phone = rs.getString("phone_number");
                record.name = rs.getString("name");
                list.add(record);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all records: " + e.getMessage());
        }
        return list;
    }


    /**
     * Deletes a CV record by phone number (Primary Key).
     */
    public static boolean deleteCV(String phoneNumber) {
        String sql = "DELETE FROM cv_records WHERE phone_number = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phoneNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting record: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates a CV record. (This example updates Name, Email, and Address).
     */
    public static boolean updateCV(CVRecord record) {
        String sql = "UPDATE cv_records SET name = ?, email = ?, address = ?, education = ?, skills = ?, work_experience = ?, projects = ? WHERE phone_number = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, record.name);
//            pstmt.setString(2, record.email);
//            pstmt.setString(3, record.address);
//            pstmt.setString(4, record.boardUniversity);
//            pstmt.setString(5, record.);
//            pstmt.setString(6, record.getWorkExperience());
//            pstmt.setString(7, record.getProjects());
//            pstmt.setString(8, record.getPhoneNumber()); // WHERE clause based on PK
//
            int rowsAffected = 0;//pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating record: " + e.getMessage());
            return false;
        }
    }
}