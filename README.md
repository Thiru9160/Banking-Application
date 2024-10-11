# Banking-Application
The banking application aims to provide users with an interface for managing their banking operations, such as fund transfers, balance inquiries, and transaction history. The application is designed to be user-friendly, ensuring ease of use for individuals with varying technical skills.

ADMIN ROLE :
             The admin can login with username and password 

  The admin should register a customer with the following details: Full name, Address, Mobile No, Email id, Type of account – Either Saving Account or Current Account, Initial Balance (min 1000), Date of Birth, Id proof 

As a result, the customer must be registered with the bank and an Account no should be generated for the customer’s account. A temporary password should also generate  

The admin should be able to add/delete/modify/see customer details, however the password and balance MUST NOT be visible to the admin 

CUSTOMER ROLE: With the help of account no and password, the customer should be able to set up 
               a new password. 

After login customer should come to customer dashboard page where the created account is displayed alongwith balance 
Clicking on View will display last 10 transactions in increasing/decreasing order of date. 

Clicking on Deposit will open a dialogue box where customer can put the amount and click submit. The balance will increase and the transaction must be saved in the database  

Clicking on Withdraw will open a dialogue box where customer can put the amount and click submit. The balance will decrease and the transaction must be saved in the database 

The customer can maintain 0 balance, but not below that 

The customer should be able to close the account on solo basis – no admin required 

KEY COMPONENTS:

AWT: Used for basic GUI components and event handling. It provides the foundation for creating user interfaces in Java.

Swing: Built on top of AWT, Swing offers a richer set of GUI components, such as buttons, tables, and forms, which enhance the visual appeal and user experience.
Database Management:

SQL: Used to interact with a relational database for data storage and retrieval. SQL statements are executed to perform operations like creating tables, inserting records, and querying data.
