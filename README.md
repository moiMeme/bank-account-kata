# Bank Account Kata

The application provides the backend for a Bank Account management that includes the following functionalities:

* deposit and Withdrawal
* Account statement (date, amount, balance)
* Statement printing

### Project Structure

| Module      | Description                                                                                                                                |
|-------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| model       | Contains the domain model, i.e., classes representing the transaction, account and client.                                                 |
| application | Contains the ports and domain services that implement the use cases. The application and model modules together form the application core. |
| adapter     | Contains the test and datasource adapters.                                                                                                 |
| bootstrap   | Instantiates adapters and domain services.                                                                                                 |

The Unit Test and InMemory datasource will be our adapters
