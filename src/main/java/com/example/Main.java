package com.example;

import java.util.List;
import java.util.Scanner;

import com.example.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {

    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        Scanner scanner = new Scanner(System.in);

        int choice;
        do {
            System.out.println("--------------------------------------------------------");
            System.out.println("------------------        MENU:        -----------------");
            System.out.println("--------------------------------------------------------");
            System.out.println("1. Dodaj studenta do bazy danych");
            System.out.println("2. Usuń studenta z bazy danych");
            System.out.println("3. Zaktualizuj studenta w bazie danych");
            System.out.println("4. Wyświetl wszystkich studentów z bazy danych");
            System.out.println("0. Wyście");
            System.out.println("--------------------------------------------------------");
            System.out.println("--------------------------------------------------------");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addUser(sessionFactory, scanner);
                    break;
                case 2:
                    deleteUser(sessionFactory, scanner);
                    break;
                case 3:
                    modifyUser(sessionFactory, scanner);
                    break;
                case 4:
                    displayUsers(sessionFactory);
                    break;
                case 0:
                    break;

                default:
                    System.out.println("Zły wybór");
                    break;
            }
        } while (choice != 0);
    }



    private static void addUser(SessionFactory sessionFactory, Scanner scanner) {
        System.out.println("Podaj imię użytkownika: ");
        String firstName = scanner.nextLine();

        System.out.println("Podaj nazwisko użytkownika: ");
        String lastName = scanner.nextLine();

        System.out.println("Podaj wiek użytkownika: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        User user = new User(firstName, lastName, age);
        session.save(user);

        session.getTransaction().commit();
        System.out.println("Dodano użytkownika o ID: " + user.getId());
    }

    private static void deleteUser(SessionFactory sessionFactory, Scanner scanner) {
        System.out.println("Podaj ID użytkownika do usunięcia: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        User user = session.get(User.class, id);
        if (user != null) {
            session.delete(user);
            System.out.println("Usunięto użytkownika o ID: " + id);
        } else {
            System.out.println("Nie znaleziono użytkownika o ID: " + id);
        }

        session.getTransaction().commit();
    }


    private static void modifyUser(SessionFactory sessionFactory, Scanner scanner) {
        System.out.println("Podaj ID użytkownika do modyfikacji: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        User user = session.get(User.class, id);
        if (user != null) {
            System.out.println("Aktualne imię: " + user.getFirstName());
            System.out.println("Podaj nowe imię (lub naciśnij enter, aby pominąć): ");
            String firstName = scanner.nextLine();
            if (!firstName.isBlank()) {
                user.setFirstName(firstName);
            }

            System.out.println("Aktualne nazwisko: " + user.getLastName());
            System.out.println("Podaj nowe nazwisko (lub naciśnij enter, aby pominąć): ");
            String lastName = scanner.nextLine();
            if (!lastName.isBlank()) {
                user.setLastName(lastName);
            }

            System.out.println("Aktualny wiek: " + user.getAge());
            System.out.println("Podaj nowy wiek (lub naciśnij enter, aby pominąć): ");
            String ageString = scanner.nextLine();
            if (!ageString.isBlank()) {
                int age = Integer.parseInt(ageString);
                user.setAge(age);
            }


            System.out.println("Zaktualizowano użytkownika o ID: " + id);
        } else {
            System.out.println("Nie znaleziono użytkownika o ID: " + id);
        }
        session.getTransaction().commit();
    }

    private static void displayUsers(SessionFactory sessionFactory) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<User> users = session.createQuery("from User", User.class).getResultList();

        System.out.println("Użytkownicy w bazie danych:");
        for (User user : users) {
            System.out.println(user);
        }

        session.getTransaction().commit();
    }
}
