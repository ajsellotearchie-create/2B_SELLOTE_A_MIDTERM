package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserMenu {

    private static final String MENU_LINE = "=".repeat(50);
    private static final String VIEW_LINE = "=".repeat(80);
    private static final String TABLE_LINE =
            "+----+--------------------+------------+--------------+----------------+";

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao dao = new UserDao();

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            switch (readInt("Enter choice: ")) {
                case 1 -> addAllocation();
                case 2 -> viewAllocations();
                case 3 -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nInvalid choice. Please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println(MENU_LINE);
        System.out.println("        STUDENT DORMITORY BED ALLOCATION");
        System.out.println(MENU_LINE);
        System.out.println("[1] Add New Allocation");
        System.out.println("[2] View All Allocations");
        System.out.println("[3] Exit System");
        System.out.println(MENU_LINE);
    }

    private void addAllocation() {
        System.out.println();
        System.out.println("--- ADD NEW BED ALLOCATION ---");

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Building Name: ");
        String building = scanner.nextLine().trim();

        System.out.print("Enter Room Number: ");
        String room = scanner.nextLine().trim();

        double rent = readDouble("Enter Monthly Rent: ");

        if (name.isEmpty() || building.isEmpty() || room.isEmpty() || rent < 0) {
            System.out.println("\nERROR: Invalid input. Allocation not saved.");
            return;
        }

        // ID is assigned by the database (starts from 1), so 0 is just a placeholder here.
        int id = dao.insert(new User(0, name, building, room, rent));
        if (id > 0) {
            System.out.println("\nSUCCESS: Dorm bed allocated successfully!");
        } else {
            System.out.println("\nERROR: Could not save the allocation to the database.");
        }
    }

    private void viewAllocations() {
        List<User> list = dao.findAll();

        System.out.println();
        System.out.println(VIEW_LINE);
        System.out.println("                         DORMITORY BED ALLOCATIONS");
        System.out.println(VIEW_LINE);

        if (list == null || list.isEmpty()) {
            System.out.println("No allocations found.");
            return;
        }

        System.out.println(TABLE_LINE);
        System.out.printf("| %-3s| %-19s| %-11s| %-13s| %-15s|%n",
                "ID", "STUDENT NAME", "BUILDING", "ROOM NO", "MONTHLY RENT");
        System.out.println(TABLE_LINE);
        for (User u : list) {
            System.out.printf("| %-3d| %-19s| %-11s| %-13s| %-15s|%n",
                    u.id(), u.studentName(), u.building(), u.roomNo(),
                    String.format("%.2f", u.monthlyRent()));
        }
        System.out.println(TABLE_LINE);
        System.out.println("Total Records Displayed: " + list.size());
    }

    /** Reads an int, then flushes the leftover newline so later nextLine() calls work. */
    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return scanner.nextInt();
        } catch (InputMismatchException ex) {
            return -1;
        } finally {
            scanner.nextLine(); // flush the rest of the line
        }
    }

    /** Reads a double, then flushes the leftover newline. Returns -1 on bad input. */
    private double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            return scanner.nextDouble();
        } catch (InputMismatchException ex) {
            return -1;
        } finally {
            scanner.nextLine(); // flush the rest of the line
        }
    }
}