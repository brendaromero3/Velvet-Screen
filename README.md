# Velvet Screen

### <ins>Project Description
This program allows a user to navigate through the movie theater application depending on the type of user they are, a client or an administrator. At any point throughout a user's navigation, they are able to visit the landing screen by clicking the logo on the top left corner or log out by clicking the log out button on the top right. The functionality is as follows:

### <ins>Clients
  Clients are able to create an account by providing the necessary information, log into their existing account, or continue as a guest. Clients are able to browse for the different movies and screenings. They are able to browse by title, language, genre, date, and time. Depending on the different search criteria, movies that meet the criteria will become available on the screen with the option to view more details about the movie. Upon clicking the "view details" button, a user is now able to see more information about the chosen movie, as well as the different screenings there are for that movie. The client is able to see the amount of seats left and book tickets if the screening is not sold out.

  Upon clicking the "book" button, the client is provided with the screening information. They are able to choose the amount of seats they would like and are provided with a breakdown of the price, such as the base price, any additional charges, and discounts. After clicking the "purchase" button, the client is able to add a payment method or choose one of their existing payment methods if they have an account. After clicking the "confirm payment" button, they have now purchased the tickets and are able to see their order confirmation. 

  If the client has an account, they are also able to see their account information. They can change any of their existing information, such as their name, address, or password. They are also able to see the amount of tickets away they are from earning a free movie! In addition to this, they are able to view, change, and delete their existing payment methods along with add new ones. 

### <ins>Administrators
   Administrators are able to view analytics about the different movies, screenings, and theaters. On their dashboard, an administrator is able to view the total revenue, total tickets solds, active screenings, and average occupancy. They are also able to see the revenue by movie and theater. 

   On the Screening page, the administrator can see the different screenings along with their information. This consists of the tickets sold, capacity, occupancy percentage, and revenue. The administrator is also able to create a new screening by providing the necessary information.

   On the Analytics page, the administrator can see more information about the theaters they manage. Along with the revenue by movie and theater, they are also able to view the revenue by screening.

   On the Movies page, the administrator can view all of the movies that are available for screening and their information. The administrator can delete a movie or add a new one after providing the necessary information. They can also see the information about all of the people involved with each movie on the Peolple screen. The administrator is able to delete a person or add a new one by providing the necessary information. 

   Lastly, on the Theaters page, the administrator can view the information about all theaters. They are able to delete a theater or add a new one after providing the necessary information. 

### <ins>How to Run
Before running the code, you must have an IDE installed. Some options include Eclipse and IntelliJ. 

Download Eclipse: [Click here!](https://www.eclipse.org/downloads/)

Download IntelliJ: [Click here!](https://www.jetbrains.com/idea/download/?section=windows)

You will also need Apache Maven: [Click here!](https://maven.apache.org/download.cgi)

> [!IMPORTANT]
> Apache Maven is required to run the program, without it, you will not be able to run it (some IDEs may come with Maven already installed).

Now onto running the program. Only cs480_client needs to run for this project.

There are two different ways to run the program:

1. **Run Configuration<br/>**
- First click "Run" on the top menu.
- Then click on "Edit Configurations..."
- Add a new Maven configuration by clicking the plus on the top left. 
- Name the run configuration whatever you would like.
- In the "Run" section, click "Modify options" and "Allow multiple instances."
- For the working directory, click the browse button, choose the cs480_client project.
- In the "command line" section, type in: 
```
clean compile exec:java
```
- Then click the blue "OK" button to finish creating the configuration.
- Now you can click the green play button (on the right of the "Run" button) to run the program

2. **Terminal**
- On the bottom left, click the "Terminal" button.
- Now cd into the directory that the project is in on your device.
- Enter the following command in the terminal to run the program:
```
mvn clean compile exec:java
```

> [!TIP]
> Creating a Run Configuration is recommended if you will be running the program multiple times!

> [!IMPORTANT]
> Before running the program, go to the GuiClient.java file and on line 254, replace the user and password with your own posrgres username and password.
