CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at DATETIME
);

CREATE TABLE expense (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         amount DOUBLE NOT NULL,
                         category VARCHAR(100),
                         description VARCHAR(255),
                         date DATETIME,
                         user_id INT NOT NULL,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);