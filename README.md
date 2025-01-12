# Web-Crawling Application

Author: [xjmaine](https://www.github.com/xjmaine/)

# General Information
1. This project is a core Java maven and React.js project
2. The project is designed to crawl web pages and extract data from them.
3. The project uses the Jsoup library to parse HTML and extract data.
4. Its a work in progress
5. you can fork the java_restapi separately which implements crud on the backend from a json file
6. the project contains a submodule found here: https://github.com/xjmaine/read_from_file_todo_app

# Project Goal
To build a scalable web-crawling application where users can input a word, partial match, or regular expression to fetch links containing the input. The application will consist of a core Java backend for crawling and data storage, and a React frontend for user interaction.

# Project Structure
1. Run the Main.java to evaluate the application
2. to read directly from "https://jsonplaceholder.typicode.com/todos", set it to the BASE_URL in the TodoClient Service class
3. To read from the json file 'database/data.json', change the variables in the Main.java file to the 'ReadFromJson.java'

# Main Project
1. Start the backend server on port 8080 or any port specified
2. Start the frontend server on port 3000
3. Open a web browser and navigate to http://localhost:3000

# Information
1. You are welcome to fork this project and make changes then push back with a pull request from your own branch