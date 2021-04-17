package pl.coderslab;
import org.apache.commons.lang3.ArrayUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) throws IOException {
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
        if(tasks.length < 1)
        {
            System.out.println(ConsoleColors.RED + "The task list is empty. Add some tasks first to remove one/" + ConsoleColors.RESET);
        }
        else {
            do{
                System.out.println(ConsoleColors.CYAN + "Type number of task from the list to remove from 1 to " + tasks.length + " from: [Type [x] to quit]" + ConsoleColors.RESET);
                listTask(tasks);
                if (scanner.hasNextLine()) {
                    String s = scanner.nextLine();
                    if(s.equals("x"))
                        return tasks;
                    try {
                        int i = Integer.parseInt(s);
                        tasks = ArrayUtils.remove(tasks, i - 1);
                        writeToDocument(tasks, "src/main/java/pl/coderslab/tasks.csv");
                        if(tasks.length == 0)
                        {
                            System.out.println(ConsoleColors.RED + "The task list is empty. Add some tasks first to remove one/" + ConsoleColors.RESET);
                            return tasks;
                        }
                    } catch (NumberFormatException a) {
                        System.out.println(ConsoleColors.RED + "It is not a integer number" + ConsoleColors.RESET);
                    } catch (IndexOutOfBoundsException b) {
                        System.out.println(ConsoleColors.RED + "It is not an option" + ConsoleColors.RESET);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }while(true);
        }
        return tasks;
    }
    public static String[][] addTask(String[][] tasks, Scanner scanner) throws IOException {
        String[] task = new String[3];
        do {
            System.out.println(ConsoleColors.YELLOW_BOLD + "Type new task description. Charcter \"[,]\" must not be used. [Type [x] to quit]" + ConsoleColors.RESET);
            String s;
            boolean correct;
            do {
                correct = true;
                s = scanner.nextLine();
                if (s.equals("x"))
                    return tasks;
                if (s.length() == 0) {
                    System.out.println(ConsoleColors.RED + "No description. Try once more. [Type [x] to quit]");
                    correct = false;
                }
                if (s.contains(",")) {
                    System.out.println(ConsoleColors.RED + "Task description contains \"[,]\" character. Try once more. [Type [x] to quit]");
                    correct = false;
                }
            } while (!correct);
            task[0] = s;
            while (true) {
                System.out.println(ConsoleColors.YELLOW_BOLD + "Type task deadline[rrrr-mm-dd]. [Type [x] to quit]" + ConsoleColors.RESET);
                s = scanner.nextLine();
                if (s.equals("x"))
                    return tasks;
                if (dateIsCorrect(s)) {
                    task[1] = s;
                    break;
                } else
                    System.out.println(ConsoleColors.RED + "Wrong data input. [Type [x] to quit]" + ConsoleColors.RESET);
            }
            System.out.println(ConsoleColors.YELLOW_BOLD + "Type if task is important [true/false]. [Type [x] to quit]" + ConsoleColors.RESET);
            do {
                s = scanner.nextLine();
                if (s.equals("x"))
                    return tasks;
                if (!(s.equals("true") || s.equals("false")))
                    System.out.println(ConsoleColors.RED + "You can type only true or false. Try once more [Type [x] to quit]");
            } while (!(s.equals("true") || s.equals("false")));
            task[2] = s;
            tasks = addRow(tasks, task);
            writeToDocument(tasks, "src/main/java/pl/coderslab/tasks.csv");
            System.out.println(ConsoleColors.YELLOW_BACKGROUND + "Task succesfully added" + ConsoleColors.RESET);
            System.out.println();
        }while(true);
    }
    public static String[][] executeOption(String[][] tasks, String option, Scanner scanner) throws IOException {
        switch (option) {
            case "add" -> tasks = addTask(tasks, scanner);
            case "remove" -> tasks = removeTask(tasks, scanner);
            case "list" -> listTask(tasks);
            case "exit" -> finish();
        }
        return tasks;
    }
    public static String chooseOption(Scanner scanner)
    {

        String option = scanner.nextLine();
        while (!(option.equals("add") || option.equals("remove") || option.equals("list") || option.equals("exit")))
        {
            System.out.println(ConsoleColors.BLUE + "Option choosen: " + option + ". Avilable: add, remove, list, exit. Try again." + ConsoleColors.RESET);
            option = scanner.nextLine();
        }
        return option;
    }
    public static void printOptions()
    {
        System.out.println(ConsoleColors.BLUE + "Please select an option." + ConsoleColors.RESET);
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
            System.out.println(ConsoleColors.RED + "list is empty. Add some tasks first." + ConsoleColors.RESET);
        else {
            System.out.print(ConsoleColors.WHITE_UNDERLINED);
            for (int i = 0; i < tasks.length; i++) {
                System.out.print((i + 1) + "]");
                for (int j = 0; j < 3; j++) {
                    System.out.print(tasks[i][j]);
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.print(ConsoleColors.RESET);
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
        strsN[strsN.length - 1][0] = s[0].trim();
        strsN[strsN.length - 1][1] = s[1].trim();
        strsN[strsN.length - 1][2] = s[2].trim();
        return strsN;
    }
    public static boolean dateIsCorrect(String date)
    {
        boolean correct = true;
        char [] dateTab = date.toCharArray();
        if(dateTab.length != 10 )
            return false;
        if(dateTab[4] != '-' || dateTab[7] != '-')
            return false;
        StringBuilder sby = new StringBuilder();
        StringBuilder sbm = new StringBuilder();
        StringBuilder sbd = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if(!(i == 4 || i == 7))
            {
                char a = dateTab[i];
                if(!Character.isDigit(a))
                    return false;
                if(i < 4)
                    sby.append(a);
                if(i>4 && i < 7)
                    sbm.append(a);
                if(i > 7)
                    sbd.append(a);
            }
        }
        String yyyy = sby.toString();
        String mm = sbm.toString();
        String dd = sbd.toString();
        int year = Integer.parseInt(yyyy);
        int month = Integer.parseInt(mm);
        int day = Integer.parseInt(dd);
        if(month > 12)
        {
            System.out.println(ConsoleColors.RED + "There are only 12 months" + ConsoleColors.RESET);
        }
        if((month == 1 || month == 3 || month == 5 || month == 7 || month ==8 || month == 10 || month == 12) && day > 31)
        {
            System.out.println(ConsoleColors.RED + "The month has 31 days" + ConsoleColors.RESET);
            return false;
        }
        if((month == 4 || month == 6 || month == 9 || month == 11) && day > 30)
        {
            System.out.println(ConsoleColors.RED + "The month has 30 days" + ConsoleColors.RESET);
            return false;
        }
        if((((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) && month == 2 && day > 29)
        {
            System.out.println(ConsoleColors.RED + "The month has 29 days in leap year" + ConsoleColors.RESET);
            return false;
        }
        if(!(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) && month == 2 && day > 28)
        {
            System.out.println(ConsoleColors.RED + "The month has 28" + ConsoleColors.RESET);
            return false;
        }
        if(year < 2015)
            System.out.println(ConsoleColors.RED + "It was long time ago. The task will probably have to be deleted" + ConsoleColors.RESET);

        return correct;
    }
    public static void writeToDocument(String [][] tasks, String path) throws IOException {
        String task;
        List<String> toWrite = new ArrayList<>();
        for (String[] t : tasks) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 3; j++) {
                sb.append(t[j]);
                if(j < 2)
                    sb.append(", ");
            }
            task = sb.toString();
            toWrite.add(task);
        }
        Path path1 = Paths.get(path);
        Files.write(path1, toWrite);
    }
}