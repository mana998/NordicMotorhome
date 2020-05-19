-- DDL


DROP DATABASE nordic;
CREATE DATABASE nordic;
USE nordic ;



-- Table country

CREATE TABLE country (
  countryID INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NULL,
  PRIMARY KEY (countryID)
);



-- Table city

CREATE TABLE IF NOT EXISTS city (
  cityID INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NULL,
  PRIMARY KEY (cityID)
  );



-- Table zip

CREATE TABLE IF NOT EXISTS zip (
  zipID INT NOT NULL AUTO_INCREMENT,
  zip VARCHAR(10) NULL,
  cityID INT NOT NULL,
  countryID INT NOT NULL,
  PRIMARY KEY (zipID),
  CONSTRAINT fk_zip_country1
    FOREIGN KEY (countryID)
    REFERENCES country (countryID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_zip_city1
    FOREIGN KEY (cityID)
    REFERENCES city (cityID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table address

CREATE TABLE IF NOT EXISTS address (
  addressID INT NOT NULL AUTO_INCREMENT,
  street VARCHAR(45) NULL,
  building INT NULL,
  floor INT NULL,
  door VARCHAR(4) NULL,
  zipID INT NOT NULL,
  PRIMARY KEY (addressID),
  CONSTRAINT fk_address_zip1
    FOREIGN KEY (zipID)
    REFERENCES zip (zipID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table renter

CREATE TABLE IF NOT EXISTS renter (
  renterID INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NULL,
  last_name VARCHAR(45) NULL,
  CPR VARCHAR(20) NULL,
  email VARCHAR(45) NULL,
  phone VARCHAR(12) NULL,
  driver_license_number VARCHAR(45) NULL,
  addressID INT NOT NULL,
  PRIMARY KEY (renterID),
  CONSTRAINT fk_Renter_Address1
    FOREIGN KEY (addressID)
    REFERENCES address (addressID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table brand

CREATE TABLE IF NOT EXISTS brand (
  brandID INT NOT NULL AUTO_INCREMENT,
  brand_name VARCHAR(12) NULL,
  PRIMARY KEY (brandID)
  );



-- Table model

CREATE TABLE IF NOT EXISTS model (
  modelID INT NOT NULL AUTO_INCREMENT,
  model_name VARCHAR(45) NULL,
  brandID INT NOT NULL,
  beds INT NULL,
  price DOUBLE NULL,
  PRIMARY KEY (modelID),
  CONSTRAINT fk_vehicle_type_brand1
    FOREIGN KEY (brandID)
    REFERENCES brand (brandID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table vehicle

CREATE TABLE IF NOT EXISTS vehicle (
  vehicleID INT NOT NULL AUTO_INCREMENT,
  plates VARCHAR(12) NULL,
  is_available TINYINT NULL,
  modelID INT NOT NULL,
  brandID INT NOT NULL,
  PRIMARY KEY (vehicleID),
  CONSTRAINT fk_vehicle_model1
    FOREIGN KEY (modelID)
    REFERENCES model (modelID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_vehicle_brand1
    FOREIGN KEY (brandID)
    REFERENCES brand (brandID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table agreement

CREATE TABLE IF NOT EXISTS agreement (
  agreementID INT NOT NULL AUTO_INCREMENT,
  renterID INT NOT NULL,
  vehicleID INT NOT NULL,
  start_date DATE NULL,
  end_date DATE NULL,
  pick_up_point INT NULL,
  drop_off_point INT NULL,
  driven_km INT NULL,
  level_of_gasoline TINYINT NULL,
  is_cancelled TINYINT NULL,
  PRIMARY KEY (agreementID),
  CONSTRAINT fk_Rental_contract_Person1
    FOREIGN KEY (renterID)
    REFERENCES renter (renterID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_Rental_contract_Car1
    FOREIGN KEY (vehicleID)
    REFERENCES vehicle (vehicleID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table job

CREATE TABLE IF NOT EXISTS job (
  jobID INT NOT NULL,
  name VARCHAR(45) NULL,
  PRIMARY KEY (jobID)
  );



-- Table users

CREATE TABLE IF NOT EXISTS users (
  userID INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NULL,
  password VARCHAR(64) NULL,
  role VARCHAR(45) NULL,
  enabled TINYINT NULL,
  PRIMARY KEY (userID)
  );



-- Table employee

CREATE TABLE IF NOT EXISTS employee (
  employeeID INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NULL,
  last_name VARCHAR(45) NULL,
  CPR VARCHAR(20) NULL,
  email VARCHAR(45) NULL,
  phone VARCHAR(12) NULL,
  addressID INT NOT NULL,
  jobID INT NOT NULL,
  salary INT NULL,
  userID INT NOT NULL,
  PRIMARY KEY (employeeID),
  CONSTRAINT fk_employee_address1
    FOREIGN KEY (addressID)
    REFERENCES address (addressID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_employee_employee_type1
    FOREIGN KEY (jobID)
    REFERENCES job (jobID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_employee_user1
    FOREIGN KEY (userID)
    REFERENCES users (userID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table extras

CREATE TABLE IF NOT EXISTS extras (
  extrasID INT NOT NULL,
  extras_name VARCHAR(45) NULL,
  extras_price DOUBLE NULL,
  PRIMARY KEY (extrasID));



-- Table agreement_has_extras

CREATE TABLE IF NOT EXISTS agreement_has_extras (
  agreementID INT NOT NULL,
  extrasID INT NOT NULL,
  quantity INT NULL,
  PRIMARY KEY (agreementID, extrasID),
  CONSTRAINT fk_agreement_has_extras_agreement1
    FOREIGN KEY (agreementID)
    REFERENCES agreement (agreementID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_agreement_has_extras_extras1
    FOREIGN KEY (extrasID)
    REFERENCES extras (extrasID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );


-- DML


USE nordic;

INSERT INTO country VALUES (1, 'Denmark'),(2, 'Sweden'),(3,'Norway');

INSERT INTO city VALUES (1, 'Copenhagen'),(2, 'Aarhus'),(3,'Odense');

INSERT INTO zip VALUES (1,1000,1,1),(2,1300,1,1),(3,1600,1,1),(4,2200,1,1);

INSERT INTO address VALUES (1,'Kronprinsessegade',23,2,'th',2),(2,'Vesterbrogade',13,3,'tv',3),(3,'Leifsgade',3,2,'tv',1),(4,'Norrebrogade',23,2,'th',4),(5,'Jagtvej',13,3,'tv',4),(6,'Lyngten',3,2,'tv',4);

INSERT INTO renter VALUES (1,'Lars','Larsen','1212785689','ll@mail.com','52814653','74920346734LK29382',1),(2,'Marc','Hansen','1506758723','mc@mail.com','34565423','749GM346734GS29382',2),(3,'Marie','Pedersen','2301895432','mp@mail.com','76904587','749GM346734GS20000',3);

INSERT INTO brand VALUES (1,'Pössl'),(2,'Sunlight'),(3,'Carado');

INSERT INTO model VALUES (1,'Trenta',1,2,200),(2,'V 66',2,4,400),(3,'T338',3,4,500);

INSERT INTO vehicle VALUES (1,'CL65542',1,1,1),(2,'XM23640',1,2,2),(3,'ZU38585',1,3,3);

INSERT INTO job VALUES (1,'sales assistant'),(2,'cleaning staff'),(3,'auto mechanic'),(4,'bookkeeper');

INSERT INTO users (userID, username, password, role, enabled) VALUES ('1', 'hello', '$2a$10$bKrHnzcBRGpgOjz86jYLxuPF/PKl2Ax/wlzfq4qYKCPdlkcBk/hZC', 'admin', '1');

INSERT INTO employee VALUES (1,'Karl','Larsen','1515785689','kl@mail.com','64814653',4,1, 800, 1),(2,'Jeanette','Pederson','1508788723','jp@mail.com','56565423',5,1,800,1),(3,'Marie','Anderson','2403895432','ma@mail.com','985454587',6,4,900,1);

INSERT INTO extras VALUES (1, 'bike rack', 100),(2, 'bed linen', 100),(3, 'child seat', 200),(4, 'picnic table and chairs', 400);

INSERT INTO agreement_has_extras VALUES (1, 1, 1);


