import java.sql.*;
import java.util.Scanner;

public class MainApp {
    public static void adaugare(Connection connection, String nume, int varsta) throws SQLException {
        String query = "INSERT INTO persoane(nume, varsta) VALUES (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nume);
            preparedStatement.setInt(2, varsta);

            preparedStatement.executeUpdate();

            System.out.println("Persoana adaugata cu succes!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verificarePersoana(int idPersoana) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab8", "root", "root")) {
            String query = "SELECT * FROM persoane WHERE id = ?";

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

    public static void adaugareExcursii(Connection connection, int id_Persoana, String destinatia, int anul) throws SQLException {
        if (!verificarePersoana(id_Persoana)) {
            System.out.println("Persoana cu id-ul :" + id_Persoana + "Nu exista! Va rugam selectati o alta persoana!");
            return;
        }

        String query = "INSERT INTO excursii(id_Persoana,destinatia,anul) values (?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id_Persoana);
            preparedStatement.setString(2, destinatia);
            preparedStatement.setInt(3, anul);

            preparedStatement.executeUpdate();

            System.out.println("Excursie adaugata cu succes!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void afisare_Persoane_excursii(Connection connection){
        String query="SELECT persoane.id, persoane.nume, persoane.varsta, excursii.id_excursie, excursii.destinatia, excursii.anul " +
                "FROM persoane  " +
                "LEFT JOIN excursii on persoane.id=excursii.id_persoana";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Executarea query-ului
            ResultSet resultSet = preparedStatement.executeQuery();

            // Afisarea rezultatelor
            while (resultSet.next()) {
                int idPersoana = resultSet.getInt("id");
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
        String query = "SELECT persoane.id, persoane.nume, excursii.destinatia, excursii.anul " +
                "FROM persoane " +
                "LEFT JOIN excursii ON persoane.id = excursii.id_persoana " +
                "WHERE persoane.nume = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, numePersoana);

            // Executarea query-ului
            ResultSet resultSet = preparedStatement.executeQuery();

            // Afisarea rezultatelor
            while (resultSet.next()) {
                int idPersoana = resultSet.getInt("id");
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
        String query = "SELECT persoane.id, persoane.nume, excursii.destinatia, excursii.anul " +
                "FROM persoane " +
                "LEFT JOIN excursii ON persoane.id = excursii.id_persoana " +
                "WHERE excursii.destinatia = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, destinatieCautata);

            // Executarea query-ului
            ResultSet resultSet = preparedStatement.executeQuery();

            // Afisarea rezultatelor
            while (resultSet.next()) {
                int idPersoana = resultSet.getInt("id");
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

    public static void afisarePersoaneAn(Connection connection, int anCautat) {
        // Query pentru a selecta persoanele care au făcut excursii într-un an specificat
        String query = "SELECT persoane.id, persoane.nume, excursii.destinatia, excursii.anul " +
                "FROM persoane " +
                "LEFT JOIN excursii ON persoane.id = excursii.id_persoana " +
                "WHERE excursii.anul = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, anCautat);

            // Executarea query-ului
            ResultSet resultSet = preparedStatement.executeQuery();

            // Afisarea rezultatelor
            while (resultSet.next()) {
                int idPersoana = resultSet.getInt("id");
                String numePersoana = resultSet.getString("nume");
                String destinatieExcursie = resultSet.getString("destinatia");
                int anExcursie = resultSet.getInt("anul");

                System.out.println("Persoana: " + numePersoana + " (ID: " + idPersoana + ")");
                System.out.println("Excursie: Destinatie: " + destinatieExcursie + ", An: " + anExcursie);
                System.out.println("------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stergereExcursie(Connection connection, int idExcursie) {
        String query = "DELETE FROM excursii WHERE id_excursie = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idExcursie);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Excursia cu ID-ul " + idExcursie + " a fost ștearsă cu succes!");
            } else {
                System.out.println("Excursia cu ID-ul " + idExcursie + " nu există în baza de date.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void stergerePersoana(Connection connection, int idPersoana) {
        String queryExcursii = "DELETE FROM excursii WHERE id_persoana = ?";
        String queryPersoana = "DELETE FROM persoane WHERE id = ?";

        try {
            connection.setAutoCommit(false); // Dezactivează modul de auto-commit

            try (PreparedStatement preparedStatementExcursii = connection.prepareStatement(queryExcursii);
                 PreparedStatement preparedStatementPersoana = connection.prepareStatement(queryPersoana)) {

                preparedStatementExcursii.setInt(1, idPersoana);
                preparedStatementPersoana.setInt(1, idPersoana);

                // Șterge excursiile asociate persoanei
                preparedStatementExcursii.executeUpdate();

                // Șterge persoana
                int affectedRowsPersoana = preparedStatementPersoana.executeUpdate();

                if (affectedRowsPersoana > 0) {
                    System.out.println("Persoana cu ID-ul " + idPersoana + " și excursiile asociate au fost șterse cu succes!");
                } else {
                    System.out.println("Persoana cu ID-ul " + idPersoana + " nu există în baza de date.");
                }

                connection.commit(); // Aplică modificările în caz de succes
            } catch (SQLException e) {
                connection.rollback(); // Anulează modificările în caz de eroare
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true); // Re-activează modul de auto-commit
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab8", "root", "root");
        Scanner scanner = new Scanner(System.in);
        afisare_Persoane_excursii(connection);
        System.out.println("Introduceti numele persoanei: ");
        String nume = scanner.nextLine();

        System.out.println("Introduceti varsta persoanei: ");
        int varsta = scanner.nextInt();

        adaugare(connection, nume, varsta);

        System.out.println("Introduceti id-ul persoanei: ");
        int id_persoana = scanner.nextInt();

        scanner.nextLine();
        System.out.println("Introduceti destinatia excursiei: ");
        String destinatie =scanner.nextLine();

        System.out.println("Introduceti anul excursiei: ");
        int an=scanner.nextInt();

        adaugareExcursii(connection,id_persoana,destinatie,an);

        afisare_Persoane_excursii(connection);

        scanner.nextLine();
        System.out.println("Dati numele persoanei careia doriti sa ii afisati excursiile: ");
        String nume_cautat=scanner.nextLine();
        afisareExcursiiPersoana(connection,nume_cautat);


        System.out.println("Dati numele destinatiei pentru a vedea persoanele care au vizitat-o");
        String destinatie_cautata= scanner.nextLine();
        afisarePersoaneDestinatie(connection,destinatie_cautata);


        System.out.println("Introduceti anul pentru a vedea persoanele care au facut excursii in acel an: ");
        int anCautat = scanner.nextInt();

        afisarePersoaneAn(connection, anCautat);

        scanner.nextLine();
        System.out.println("Introduceti ID-ul excursiei pe care doriti sa o stergeti: ");
        int idExcursieStergere = scanner.nextInt();

        stergereExcursie(connection, idExcursieStergere);

        afisare_Persoane_excursii(connection); // Pentru a afișa starea actualizată după ștergere

        System.out.println("Introduceti ID-ul persoanei pe care doriti sa o stergeti: ");
        int idPersoanaStergere = scanner.nextInt();

        stergerePersoana(connection, idPersoanaStergere);

        afisare_Persoane_excursii(connection); // Pentru a afișa starea actualizată după ștergere
    }
}
