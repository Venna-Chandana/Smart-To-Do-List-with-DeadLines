package com.gqt.core_java.Mini_projects;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Task {
    String title;
    String description;
    LocalDate deadline;
    String priority;
    boolean completed;

    Task(String title, String description, LocalDate deadline, String priority) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.completed = false;
    }

    @Override
    public String toString() {
        String status;
        if (completed) {
            status = SmartToDoList.GREEN + "Completed" + SmartToDoList.RESET;
        } else if (deadline.isBefore(LocalDate.now())) {
            status = SmartToDoList.RED + "Overdue" + SmartToDoList.RESET;
        } else {
            status = SmartToDoList.YELLOW + "Pending" + SmartToDoList.RESET;
        }

        return SmartToDoList.CYAN +
               "Title: " + title +
               "\nDescription: " + description +
               "\nDeadline: " + deadline +
               "\nPriority: " + priority +
               "\nStatus: " + status + SmartToDoList.RESET + "\n";
    }
}

public class SmartToDoList {
    static Scanner sc = new Scanner(System.in);
    static List<Task> tasks = new ArrayList<>();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ANSI Color Codes
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        while (true) {
            System.out.println(CYAN + "\n=== SMART TO-DO LIST MENU ===" + RESET);
            System.out.println("1. Add Task");
            System.out.println("2. View All Tasks");
            System.out.println("3. Mark Task as Completed");
            System.out.println("4. Save Tasks to File");
            System.out.println("5. Exit");
            System.out.print(YELLOW + "Choose an option: " + RESET);

            int choice = getIntInput();
            switch (choice) {
                case 1: addTask(); break;
                case 2: viewTasks(); break;
                case 3: markTaskCompleted(); break;
                case 4: saveTasksToFile(); break;
                case 5: System.out.println(GREEN + "Exiting program. Goodbye!" + RESET); return;
                default: System.out.println(RED + "Invalid choice. Try again." + RESET);
            }
        }
    }

    static void addTask() {
        System.out.print(YELLOW + "Enter task title: " + RESET);
        String title = sc.nextLine();

        System.out.print(YELLOW + "Enter task description: " + RESET);
        String description = sc.nextLine();

        System.out.print(YELLOW + "Enter deadline (yyyy-MM-dd): " + RESET);
        String dateStr = sc.nextLine();
        LocalDate deadline;
        try {
            deadline = LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            System.out.println(RED + "Invalid date format. Task not added." + RESET);
            return;
        }

        if (deadline.isBefore(LocalDate.now())) {
            System.out.println(RED + "Deadline is in the past. Task not added." + RESET);
            return;
        }

        System.out.print(YELLOW + "Enter priority (High/Medium/Low): " + RESET);
        String priority = sc.nextLine();

        Task task = new Task(title, description, deadline, priority);
        tasks.add(task);
        System.out.println(GREEN + "Task added successfully." + RESET);
    }

    static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println(RED + "No tasks available." + RESET);
            return;
        }

        tasks.sort(Comparator.comparing(task -> task.deadline));
        System.out.println(CYAN + "\n=== All Tasks Sorted by Deadline ===" + RESET);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(CYAN + "Task #" + (i + 1) + RESET);
            System.out.println(tasks.get(i));
        }
    }

    static void markTaskCompleted() {
        viewTasks();
        if (tasks.isEmpty()) return;

        System.out.print(YELLOW + "Enter the task number to mark as completed: " + RESET);
        int index = getIntInput();
        if (index < 1 || index > tasks.size()) {
            System.out.println(RED + "Invalid task number." + RESET);
            return;
        }

        tasks.get(index - 1).completed = true;
        System.out.println(GREEN + "Task marked as completed." + RESET);
    }

    static void saveTasksToFile() {
        try (FileWriter fw = new FileWriter("todo_list.txt")) {
            for (Task task : tasks) {
                fw.write(task.toString().replaceAll("\u001B\\[[;\\d]*m", "")); // remove ANSI for file
                fw.write("----------\n");
            }
            System.out.println(GREEN + "Tasks saved to todo_list.txt successfully." + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Error writing to file." + RESET);
        }
    }

    static int getIntInput() {
        while (!sc.hasNextInt()) {
            System.out.print(RED + "Please enter a valid number: " + RESET);
            sc.nextLine(); // clear invalid input
        }
        int num = sc.nextInt();
        sc.nextLine(); // clear newline
        return num;
    }
}




































