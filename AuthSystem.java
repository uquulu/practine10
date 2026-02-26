import java.util.Scanner;


class UserLimitExceededException extends Exception {
    public UserLimitExceededException(String message) {
        super(message);
    }
}

class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}

class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}

public class AuthSystem {

    static String[] usernames = new String[15];
    static String[] passwords = new String[15];
    static int userCount = 0;

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            showMenu();

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        deleteUser();
                        break;
                    case 3:
                        authenticateUser();
                        break;
                    case 4:
                        System.out.println("Програма завершена.");
                        return;
                    default:
                        System.out.println("Невірний вибір!");
                }

            } catch (NumberFormatException e) {
                System.out.println("Помилка: потрібно вводити число!");
            }
        }
    }


    static void showMenu() {
        System.out.println("\n1 - Додати користувача");
        System.out.println("2 - Видалити користувача");
        System.out.println("3 - Виконати дію (аутентифікація)");
        System.out.println("4 - Вийти");
        System.out.print("Ваш вибір: ");
    }

    static void registerUser() {
        try {

            if (userCount >= 15) {
                throw new UserLimitExceededException("Досягнуто максимум 15 користувачів!");
            }

            System.out.print("Введіть ім'я користувача: ");
            String username = scanner.nextLine();
            validateUsername(username);

            if (findUser(username) != -1) {
                throw new InvalidUsernameException("Користувач з таким ім'ям вже існує!");
            }

            System.out.print("Введіть пароль: ");
            String password = scanner.nextLine();
            validatePassword(password);

            int index = getFreeIndex();
            usernames[index] = username;
            passwords[index] = password;
            userCount++;

            System.out.println("Користувач успішно зареєстрований!");

        } catch (UserLimitExceededException |
                 InvalidUsernameException |
                 InvalidPasswordException e) {

            System.out.println("Помилка: " + e.getMessage());
        }
    }

    static void deleteUser() {
        try {
            System.out.print("Введіть ім'я користувача для видалення: ");
            String username = scanner.nextLine();

            int index = findUser(username);
            if (index == -1) {
                throw new UserNotFoundException("Користувача не знайдено!");
            }

            usernames[index] = null;
            passwords[index] = null;
            userCount--;

            System.out.println("Користувача видалено!");

        } catch (UserNotFoundException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }


    static void authenticateUser() {
        try {
            System.out.print("Введіть ім'я користувача: ");
            String username = scanner.nextLine();

            System.out.print("Введіть пароль: ");
            String password = scanner.nextLine();

            int index = findUser(username);

            if (index == -1) {
                throw new AuthenticationException("Невірне ім'я або пароль!");
            }

            if (!passwords[index].equals(password)) {
                throw new AuthenticationException("Невірне ім'я або пароль!");
            }

            System.out.println("Користувача успішно аутентифіковано!");

        } catch (AuthenticationException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }


    static void validateUsername(String username) throws InvalidUsernameException {

        if (username.length() < 5) {
            throw new InvalidUsernameException("Ім'я повинно містити мінімум 5 символів!");
        }

        for (int i = 0; i < username.length(); i++) {
            if (username.charAt(i) == ' ') {
                throw new InvalidUsernameException("Ім'я не повинно містити пробілів!");
            }
        }
    }

    static void validatePassword(String password) throws InvalidPasswordException {

        if (password.length() < 10) {
            throw new InvalidPasswordException("Пароль має бути мінімум 10 символів!");
        }

        if (containsForbiddenWord(password)) {
            throw new InvalidPasswordException("Пароль містить заборонене слово!");
        }

        int digitCount = 0;
        int specialCount = 0;

        for (int i = 0; i < password.length(); i++) {

            char c = password.charAt(i);

            if (c == ' ') {
                throw new InvalidPasswordException("Пароль не повинен містити пробілів!");
            }

            if (c >= '0' && c <= '9') {
                digitCount++;
            } else if ((c >= 'A' && c <= 'Z') ||
                    (c >= 'a' && c <= 'z')) {
                // ok
            } else {
                specialCount++;
            }
        }

        if (digitCount < 3) {
            throw new InvalidPasswordException("Пароль повинен містити мінімум 3 цифри!");
        }

        if (specialCount < 1) {
            throw new InvalidPasswordException("Пароль повинен містити мінімум 1 спеціальний символ!");
        }
    }

    static boolean containsForbiddenWord(String password) {

        String[] forbidden = {"admin", "pass", "password", "qwerty", "ytrewq"};

        String lower = password.toLowerCase();

        for (int i = 0; i < forbidden.length; i++) {
            if (lower.contains(forbidden[i])) {
                return true;
            }
        }

        return false;
    }


    static int findUser(String username) {
        for (int i = 0; i < usernames.length; i++) {
            if (usernames[i] != null && usernames[i].equals(username)) {
                return i;
            }
        }
        return -1;
    }

    static int getFreeIndex() {
        for (int i = 0; i < usernames.length; i++) {
            if (usernames[i] == null) {
                return i;
            }
        }
        return -1;
    }
}