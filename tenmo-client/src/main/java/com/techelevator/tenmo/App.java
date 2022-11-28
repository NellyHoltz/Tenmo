package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final UserService userService = new UserService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService();
    private final AccountService accountService = new AccountService();


    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        String token = currentUser.getToken();
        userService.setAuthToken(token);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		double currentBalance = userService.viewCurrentBalance(currentUser.getUser().getId());
        System.out.println("Your current balance is $" + currentBalance + ".");
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        Transfer[] transfers= transferService.getTransfers(currentUser.getUser().getId(), currentUser.getToken());
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.printf("%-5s %10s %10s %15s%n", "ID", "From", "To", "Amount");
        System.out.println("-------------------------------------------");

        for(Transfer transfer: transfers) {
            System.out.print(transfer.toSummaryString());
        }
        System.out.println();
        int requestedTransferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        if (requestedTransferId == 0) {
            return;
        }
        Transfer requestedTransfer = null;
        for (Transfer transfer : transfers) {
            if (transfer.getTransferId() == requestedTransferId) {
                requestedTransfer = transfer;
            }
        }
        if (requestedTransfer == null) {
            System.out.println("Invalid transfer Id!");
            return;
        }
        System.out.println("-------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------");
        System.out.println(requestedTransfer.toString());
		
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
        Account[] accounts = accountService.getAccounts(currentUser.getUser().getId(), currentUser.getToken());
        Account currentUsersAccount = accountService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken());
        double currentUserBalance = userService.viewCurrentBalance(currentUser.getUser().getId());
        System.out.println("-------------------------");
        System.out.println("Users");
        System.out.println("-------------------------");
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
        System.out.println();
        int sendToId = consoleService.promptForInt("Enter the ID of the user you are sending to (0 to cancel): ");
        if (sendToId == 0) {
            return;
        }
        if (sendToId == currentUsersAccount.getId()) {
            System.out.println("You can't send money to the account you are transferring from!");
            System.out.println();
            return;
        }
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to send: ");
        double doubleAmount = Double.parseDouble(amount.toString());
        if (currentUserBalance < doubleAmount) {
            System.out.println("INSUFFICIENT FUNDS!");
            System.out.println();
            return;
        }
        if (doubleAmount <= 0) {
            System.out.println("Must enter a positive amount!");
            System.out.println();
            return;
        }
        System.out.println("Transaction Approved");
        Account sendFromAccount = accountService.getAccountByAccountId(currentUsersAccount.getId(), currentUser.getToken());
        Account sendToAccount = accountService.getAccountByAccountId(sendToId, currentUser.getToken());
        Transfer transfer = new Transfer(2,2, sendFromAccount, sendToAccount, doubleAmount);
        Transfer returnedTransfer = transferService.createTransfer(transfer, currentUser.getToken());
        boolean outcome = accountService.updateBalances(returnedTransfer, currentUser.getToken());
        if (outcome = false) {
            System.out.println("Something is wrong.");
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
