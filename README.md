# Tuxedo-Server-API
This is an implementation of securely exposing Mainframe DB2 database to internet

This implementation consist of,
1. Server program that establishes connection with configured Tuxedo server which is intern connected with mainframe DB2 database
2. /www folder with html pages to get query inputs from user

### How to use it?
For this you require a IBM mainframe with DB2 database, wrapped with tuxedo server
1. Configure the IP address of tuxedo server
2. Configure query that need to be executed (or change the html page to get query from user)
3. Start the server program
4. Get the index page from server via http from your browser
5. Enter values to complete the query
6. Press send
7. Result will be updated below
