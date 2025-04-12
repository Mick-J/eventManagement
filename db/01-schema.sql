DROP DATABASE IF EXISTS event_db;

CREATE SCHEMA `event_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE event_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS user_company, company_attendee, lead_event_tech, lead_event, event_tech, company, country, event_type, user;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS country (
   country_id INT AUTO_INCREMENT PRIMARY KEY,
   country_name VARCHAR(150),
   country_code VARCHAR(100),
   country_dial_code VARCHAR(100),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

);

CREATE TABLE IF NOT EXISTS company (
   company_id INT AUTO_INCREMENT PRIMARY KEY,
   company_name VARCHAR(255) NOT NULL UNIQUE,
   website VARCHAR(255),
   country_id INT NOT NULL,
   contact_email VARCHAR(255) UNIQUE,
   company_phone VARCHAR(20),
   company_fax VARCHAR(20),
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (country_id) REFERENCES country(country_id)
);

CREATE TABLE IF NOT EXISTS user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_firstname VARCHAR(255) NOT NULL,
    user_lastname VARCHAR(255) NOT NULL,
    user_password VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    user_phone VARCHAR(20),
    user_active VARCHAR(1) DEFAULT 'Y',  -- active: Y: yes, N: no
    user_role VARCHAR(6) DEFAULT 'USER',  -- Roles: ADMIN, USER
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- user_company: which company subsidiary the user work for
CREATE TABLE IF NOT EXISTS user_company (
    user_company_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    company_id INT NOT NULL,
    date_recruited DATE,
    job_title  VARCHAR(200),
    departement  VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (company_id) REFERENCES company(company_id)
);


CREATE TABLE IF NOT EXISTS event_type (
      event_type_id INT AUTO_INCREMENT PRIMARY KEY,
      event_type VARCHAR(20), -- '1-CONFERENCE', 2-'EXHIBITION',  3-'MEETING'
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS event_tech (  -- event_tech instead of event, event is a mysql reserved keyword
      event_id INT AUTO_INCREMENT PRIMARY KEY,
      event_type_id INT NOT NULL DEFAULT 2,
      company_id INT NOT NULL,
      event_responsible_id INT NOT NULL,
      event_title VARCHAR(255) NOT NULL,
      event_description VARCHAR(500),
      event_date_begin VARCHAR(10) NOT NULL,
      event_date_end VARCHAR(10) NOT NULL,
      event_time_begin VARCHAR(10) NOT NULL,
      event_time_end VARCHAR(10) NOT NULL,
      event_location VARCHAR(255) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      FOREIGN KEY (company_id) REFERENCES company(company_id),
      FOREIGN KEY (event_type_id) REFERENCES event_type(event_type_id),
      FOREIGN KEY (event_responsible_id) REFERENCES user(user_id)
);

CREATE TABLE IF NOT EXISTS company_attendee (
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    PRIMARY KEY (user_id, event_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES event_tech(event_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS lead_event (
      lead_id INT AUTO_INCREMENT PRIMARY KEY,
      lead_firstname VARCHAR(255),
      lead_lastname VARCHAR(255),
      lead_email VARCHAR(255) NOT NULL UNIQUE,
      lead_phone VARCHAR(20),
      lead_company_name VARCHAR(255),
      lead_interest_level VARCHAR(10) DEFAULT 'MEDIUM', -- 'HIGH', 'MEDIUM', 'LOW'
      comment  VARCHAR(500),
      lead_state VARCHAR(10) DEFAULT 'PENDING', -- 'PENDING', 'PROCESSED', 'IGNORE'
      lead_b_card_img VARCHAR(200), -- business card image name (name+extention)
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE lead_event_tech (
    event_id INT NOT NULL,
    lead_id INT NOT NULL,
    PRIMARY KEY (event_id, lead_id),
    FOREIGN KEY (event_id) REFERENCES event_tech(event_id) ON DELETE CASCADE,
    FOREIGN KEY (lead_id) REFERENCES lead_event(lead_id) ON DELETE CASCADE
);