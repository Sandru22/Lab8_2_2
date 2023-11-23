package exercitiu1;

import java.sql.*;
import java.util.Scanner;

class MainApp {
    public static void adaugare(Connection connection,String nume,int varsta){

            // Query pentru inserarea unei persoane
            String query = "INSERT INTO persoane (nume, varsta) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nume);
                preparedStatement.setInt(2, varsta);

                // Executarea query-ului
                preparedStatement.executeUpdate();

                System.out.println("Persoana adaugata cu succes!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

    }

    public static boolean verificaPersoanaExistenta(int idPersoana) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab8", "root", "root")) {
            // Query pentru verificarea existentei unei persoane
            String query = "SELECT * FROM persoane WHERE id_persoana = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, idPersoana);

                // Executarea query-ului
                ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next(); // Returneaza true daca exista o inregistrare, altfel false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void adaugare_excursii(Connection connection,int idPersoana,String destinatia,int anul){
        if (!verificaPersoanaExistenta(idPersoana)) {
            System.out.println("Persoana cu id-ul " + idPersoana + " nu exista. Adaugati persoana inainte de a adauga excursia.");
            return;
        }
        String query = "INSERT INTO excursii (id_persoana,destinatia, anul) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idPersoana);
            preparedStatement.setString(2, destinatia);
            preparedStatement.setInt(3, anul);

            // Executarea query-ului
            preparedStatement.executeUpdate();

            System.out.println("Excursie adaugata cu succes!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void afisarePersoaneExcursii(Connection connection) {
        // Query pentru a selecta toate persoanele și excursiile asociate
        String query = "SELECT persoane.id_persoana, persoane.nume, persoane.varsta, excursii.destinatia, excursii.anul " +
                "FROM persoane " +
                "LEFT JOIN excursii ON persoane.id_persoana = excursii.id_persoana";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Executarea query-ului
            ResultSet resultSet = preparedStatement.executeQuery();

            // Afisarea rezultatelor
            while (resultSet.next()) {
                int idPersoana = resultSet.getInt("id_persoana");
                String numePersoana = resultSet.getString("nume");
                int varstaPersoana = resultSet.getInt("varsta");
                String destinatieExcursie = resultSet.getString("destinatia");
                int anExcursie = resultSet.getInt("anul");

                System.out.println("Persoana: " + numePersoana + " (ID: " + idPersoana + ", Varsta: " + varstaPersoana + ")");
                System.out.println("Excursie: " + destinatieExcursie + ", An: " + anExcursie);
                System.out.println("------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void afisareExcursiiPersoana(Connection connection, String numePersoana) {
        // Query pentru a selecta excursiile pentru o anumită persoană
        String query = "SELECT persoane.id_persoana, persoane.nume, excursii.destinatia, excursii.anul " +
                "FROM persoane " +
                "LEFT JOIN excursii ON persoane.id_persoana = excursii.id_persoana " +
                "WHERE persoane.nume = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, numePersoana);

            // Executarea query-ului
            ResultSet resultSet = preparedStatement.executeQuery();

            // Afisarea rezultatelor
            while (resultSet.next()) {
                int idPersoana = resultSet.getInt("id_persoana");
                String destinatieExcursie = resultSet.getString("destinatia");
                int anExcursie = resultSet.getInt("anul");

                System.out.println("Persoana: " + numePersoana + " (ID: " + idPersoana + ")");
                System.out.println("Excursie: " + destinatieExcursie + ", An: " + anExcursie);
                System.out.println("------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void afisarePersoaneDestinatie(Connection connection, String destinatieCautata) {
        // Query pentru a selecta persoanele care au vizitat o anumită destinație
        String query = "SELECT persoane.id_persoana, persoane.nume, excursii.destinatia, excursii.anul " +
                "FROM persoane " +
                "LEFT JOIN excursii ON persoane.id_persoana = excursii.id_persoana " +
                "WHERE excursii.destinatia = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, destinatieCautata);

            // Executarea query-ului
            ResultSet resultSet = preparedStatement.executeQuery();

            // Afisarea rezultatelor
            while (resultSet.next()) {
                int idPersoana = resultSet.getInt("id_persoana");
                String numePersoana = resultSet.getString("nume");
                int anExcursie = resultSet.getInt("anul");

                System.out.println("Persoana: " + numePersoana + " (ID: " + idPersoana + ")");
                System.out.println("Excursie: Destinatie: " + destinatieCautata + ", An: " + anExcursie);
                System.out.println("------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab8", "root", "root");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduceti numele persoanei:");
        String nume = scanner.nextLine();

        System.out.println("Introduceti varsta persoanei:");
        int varsta = scanner.nextInt();
adaugare(connection,nume,varsta);

        System.out.println("Introduceti id-ul persoanei:");
        int id_persoana = scanner.nextInt();

        scanner.nextLine();
        System.out.println("Introduceti numele destinatiei:");
        String destinatie = scanner.nextLine();

        System.out.println("Introduceti anul excursiei:");
        int anul = scanner.nextInt();

        adaugare_excursii(connection,id_persoana,destinatie,anul);

        afisarePersoaneExcursii(connection);

        scanner.nextLine();
        System.out.println("Dati numele persoanei careia doriti sa ii afisati excursiile: ");
        String nume_cautat=scanner.nextLine();
        afisareExcursiiPersoana(connection,nume_cautat);

        scanner.nextLine();
        System.out.println("Dati numele destinatiei pentru a vedea persoanele care au vizitat-o");
        String destinatie_cautata= scanner.nextLine();
        afisarePersoaneDestinatie(connection,destinatie_cautata);
    }
}
