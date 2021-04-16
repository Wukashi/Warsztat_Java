package pl.coderslab;
import org.apache.commons.lang3.ArrayUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) throws FileNotFoundException {
        String [][] tasks;
        File file  = new File("src/main/java/pl/coderslab/tasks.csv");
        Scanner scanner = new Scanner(file);
        tasks = getDataFromFile(scanner);
        scanner.close();
        String option;
        Scanner scanner1 = new Scanner(System.in);
        do{
            printOptions();
            option = chooseOption(scanner1);
            tasks = executeOption(tasks, option, scanner1);
        }while(!option.equals("exit"));
        scanner1.close();
    }
    public static String [][] removeTask(String [][] tasks, Scanner scanner)
    {
        System.out.println(ConsoleColors.CYAN + "Type number of task from the list to remove from 1 to " + tasks.length + ConsoleColors.RESET);
        if(scanner.hasNextInt())
        {
            int i = scanner.nextInt();
            if(i >= 0)
            {
                if(i <= tasks.length)
                {
                    tasks = ArrayUtils.remove(tasks, i - 1);
                }
                else
                {
                    System.out.println(ConsoleColors.RED + "There are not so many tasks on the list. There are " + tasks.length + " there." + ConsoleColors.RESET);
                }
            }
            else
            {
                System.out.println(ConsoleColors.RED + "The number can not be negative. Negative number o tasks? I wish that" + ConsoleColors.RESET);
            }
        }
        else
        {
            System.out.println(ConsoleColors.RED + "It is not a integer number." + ConsoleColors.RESET);
        }
        return tasks;
    }
    public static String[][] addTask(String[][] tasks, Scanner scanner)
    {
        String [] task = new String[3];
        System.out.println(ConsoleColors.YELLOW_BOLD + "Type task description" + ConsoleColors.RESET);
        task[0] = scanner.nextLine();
        while (true)
        {
        System.out.println(ConsoleColors.YELLOW_BOLD +"Type task deadline[rrrr-mm-dd]" + ConsoleColors.RESET);
            task[1] = scanner.nextLine();
            char [] date = task[1].toCharArray();
            boolean correct = true;
            if(date.length != 10)
                correct = false;
            if(correct)
                break;
            else
                System.out.println(ConsoleColors.RED + "Wrong data input" + ConsoleColors.RESET);
        }
        System.out.println(ConsoleColors.YELLOW_BOLD +"Type if task is important [true/false]" + ConsoleColors.RESET);
        task[2] = scanner.nextLine();
        return addRow(tasks, task);
    }
    public static String[][] executeOption(String[][] tasks, String option, Scanner scanner)
    {
        switch (option) {
            case "add":
                tasks = addTask(tasks, scanner);
                break;
            case "remove":
                tasks = removeTask(tasks, scanner);
                break;
            case "list":
                listTask(tasks);
                break;
            case "exit":
                finish();
                break;
        }
        return tasks;
    }
    public static String chooseOption(Scanner scanner)
    {

        String option = scanner.nextLine();
        while (!(option.equals("add") || option.equals("remove") || option.equals("list") || option.equals("exit")))
        {
            System.out.println("Option choosen: " + option + ". Avilable: add, remove, list, exit. Try again.");
            option = scanner.nextLine();
        }
        return option;
    }
    public static void printOptions()
    {
        System.out.println(ConsoleColors.BLUE + "Please select an option" + ConsoleColors.RESET);
        String [] opcje = {"add", "remove", "list", "exit"};
        for (String s : opcje) {
            System.out.println(ConsoleColors.PURPLE + s + ConsoleColors.RESET);
        }
    }
    public static String[][] getDataFromFile(Scanner scanner) {
        String [][] tasks = new String[0][3];
        while(scanner.hasNextLine())
        {
            String s = scanner.nextLine();
            String [] strs = s.split("[,]");
            tasks = addRow(tasks,strs);
        }

        return tasks;
    }
    public static void listTask(String[][] tasks)
    {
        if(tasks.length == 0)
            System.out.println("list is empty. Add some tasks first.");
        else {
            for (int i = 0; i < tasks.length; i++) {
                System.out.print((i + 1) + "]");
                for (int j = 0; j < 3; j++) {
                    System.out.print(tasks[i][j]);
                    System.out.print(" ");
                }
                System.out.println();
            }
        }
    }
    public static void finish()
    {
        System.out.println(ConsoleColors.RED + "bye bye ;(" + ConsoleColors.RESET);
    }
    public static String [][] addRow(String[][] strs, String[] s)
    {
        String [][] strsN = new String [strs.length + 1][3];
        for (int i = 0; i < strs.length; i++) {
            strsN[i] = strs[i];
        }
        strsN[strsN.length - 1][0] = s[0];
        strsN[strsN.length - 1][1] = s[1];
        strsN[strsN.length - 1][2] = s[2];
        return strsN;
    }
}