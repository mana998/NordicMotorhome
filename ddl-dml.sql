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
  phone VARCHAR(20) NULL,
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
  modelID INT,
  brandID INT NOT NULL,
  PRIMARY KEY (vehicleID),
  CONSTRAINT fk_vehicle_model1
    FOREIGN KEY (modelID)
    REFERENCES model (modelID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_vehicle_brand1
    FOREIGN KEY (brandID)
    REFERENCES brand (brandID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );



-- Table agreement

CREATE TABLE IF NOT EXISTS agreement (
  agreementID INT NOT NULL AUTO_INCREMENT,
  renterID INT NULL,
  vehicleID INT,
  start_date DATE NULL,
  end_date DATE NULL,
  pick_up_point DOUBLE NULL,
  drop_off_point DOUBLE NULL,
  driven_km INT NULL,
  level_of_gasoline TINYINT NULL,
  is_cancelled TINYINT DEFAULT 0,
  PRIMARY KEY (agreementID),
  CONSTRAINT fk_Rental_contract_Person1
    FOREIGN KEY (renterID)
    REFERENCES renter (renterID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT fk_Rental_contract_Car1
    FOREIGN KEY (vehicleID)
    REFERENCES vehicle (vehicleID)
    ON DELETE SET NULL
    ON UPDATE CASCADE
    );



-- Table job

CREATE TABLE IF NOT EXISTS job (
  jobID INT NOT NULL AUTO_INCREMENT,
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
  phone VARCHAR(20) NULL,
  addressID INT NOT NULL,
  jobID INT NOT NULL,
  salary INT NULL,
  userID INT NOT NULL,
  PRIMARY KEY (employeeID),
  CONSTRAINT fk_employee_address1
    FOREIGN KEY (addressID)
    REFERENCES address (addressID)
    ON DELETE CASCADE
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
  quantity INT NOT NULL DEFAULT 0,
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

INSERT INTO `country` VALUES (null,   'Afghanistan');
INSERT INTO `country` VALUES (null,   'Albania');
INSERT INTO `country` VALUES (null,   'Algeria');
INSERT INTO `country` VALUES (null,   'American Samoa');
INSERT INTO `country` VALUES (null,   'Andorra');
INSERT INTO `country` VALUES (null,   'Angola');
INSERT INTO `country` VALUES (null,   'Anguilla');
INSERT INTO `country` VALUES (null,   'Antarctica');
INSERT INTO `country` VALUES (null,   'Antigua and Barbuda');
INSERT INTO `country` VALUES (null,   'Argentina');
INSERT INTO `country` VALUES (null,   'Armenia');
INSERT INTO `country` VALUES (null,   'Aruba');
INSERT INTO `country` VALUES (null,   'Australia');
INSERT INTO `country` VALUES (null,   'Austria');
INSERT INTO `country` VALUES (null,   'Azerbaijan');
INSERT INTO `country` VALUES (null,   'Bahamas');
INSERT INTO `country` VALUES (null,   'Bahrain');
INSERT INTO `country` VALUES (null,   'Bangladesh');
INSERT INTO `country` VALUES (null,   'Barbados');
INSERT INTO `country` VALUES (null,   'Belarus');
INSERT INTO `country` VALUES (null,   'Belgium');
INSERT INTO `country` VALUES (null,   'Belize');
INSERT INTO `country` VALUES (null,   'Benin');
INSERT INTO `country` VALUES (null,   'Bermuda');
INSERT INTO `country` VALUES (null,   'Bhutan');
INSERT INTO `country` VALUES (null,   'Bolivia');
INSERT INTO `country` VALUES (null,   'Bosnia and Herzegovina');
INSERT INTO `country` VALUES (null,   'Botswana');
INSERT INTO `country` VALUES (null,   'Bouvet Island');
INSERT INTO `country` VALUES (null,   'Brazil');
INSERT INTO `country` VALUES (null,   'British Indian Ocean Territory');
INSERT INTO `country` VALUES (null,   'Brunei Darussalam');
INSERT INTO `country` VALUES (null,   'Bulgaria');
INSERT INTO `country` VALUES (null,   'Burkina Faso');
INSERT INTO `country` VALUES (null,   'Burundi');
INSERT INTO `country` VALUES (null,   'Cambodia');
INSERT INTO `country` VALUES (null,   'Cameroon');
INSERT INTO `country` VALUES (null,   'Canada');
INSERT INTO `country` VALUES (null,   'Cape Verde');
INSERT INTO `country` VALUES (null,   'Cayman Islands');
INSERT INTO `country` VALUES (null,   'Central African Republic');
INSERT INTO `country` VALUES (null,   'Chad');
INSERT INTO `country` VALUES (null,   'Chile');
INSERT INTO `country` VALUES (null,   'China');
INSERT INTO `country` VALUES (null,   'Christmas Island');
INSERT INTO `country` VALUES (null,   'Cocos (Keeling) Islands');
INSERT INTO `country` VALUES (null,   'Colombia');
INSERT INTO `country` VALUES (null,   'Comoros');
INSERT INTO `country` VALUES (null,   'Democratic Republic of the Congo');
INSERT INTO `country` VALUES (null,   'Republic of Congo');
INSERT INTO `country` VALUES (null,   'Cook Islands');
INSERT INTO `country` VALUES (null,   'Costa Rica');
INSERT INTO `country` VALUES (null,   'Croatia (Hrvatska)');
INSERT INTO `country` VALUES (null,   'Cuba');
INSERT INTO `country` VALUES (null,   'Cyprus');
INSERT INTO `country` VALUES (null,   'Czech Republic');
INSERT INTO `country` VALUES (null,   'Denmark');
INSERT INTO `country` VALUES (null,   'Djibouti');
INSERT INTO `country` VALUES (null,   'Dominica');
INSERT INTO `country` VALUES (null,   'Dominican Republic');
INSERT INTO `country` VALUES (null,   'East Timor');
INSERT INTO `country` VALUES (null,   'Ecuador');
INSERT INTO `country` VALUES (null,   'Egypt');
INSERT INTO `country` VALUES (null,   'El Salvador');
INSERT INTO `country` VALUES (null,   'Equatorial Guinea');
INSERT INTO `country` VALUES (null,   'Eritrea');
INSERT INTO `country` VALUES (null,   'Estonia');
INSERT INTO `country` VALUES (null,   'Ethiopia');
INSERT INTO `country` VALUES (null,   'Falkland Islands (Malvinas)');
INSERT INTO `country` VALUES (null,   'Faroe Islands');
INSERT INTO `country` VALUES (null,   'Fiji');
INSERT INTO `country` VALUES (null,   'Finland');
INSERT INTO `country` VALUES (null,   'France');
INSERT INTO `country` VALUES (null,   'France, Metropolitan');
INSERT INTO `country` VALUES (null,   'French Guiana');
INSERT INTO `country` VALUES (null,   'French Polynesia');
INSERT INTO `country` VALUES (null,   'French Southern Territories');
INSERT INTO `country` VALUES (null,   'Gabon');
INSERT INTO `country` VALUES (null,   'Gambia');
INSERT INTO `country` VALUES (null,   'Georgia');
INSERT INTO `country` VALUES (null,   'Germany');
INSERT INTO `country` VALUES (null,   'Ghana');
INSERT INTO `country` VALUES (null,   'Gibraltar');
INSERT INTO `country` VALUES (null,   'Guernsey');
INSERT INTO `country` VALUES (null,   'Greece');
INSERT INTO `country` VALUES (null,   'Greenland');
INSERT INTO `country` VALUES (null,   'Grenada');
INSERT INTO `country` VALUES (null,   'Guadeloupe');
INSERT INTO `country` VALUES (null,   'Guam');
INSERT INTO `country` VALUES (null,   'Guatemala');
INSERT INTO `country` VALUES (null,   'Guinea');
INSERT INTO `country` VALUES (null,   'Guinea-Bissau');
INSERT INTO `country` VALUES (null,   'Guyana');
INSERT INTO `country` VALUES (null,   'Haiti');
INSERT INTO `country` VALUES (null,   'Heard and Mc Donald Islands');
INSERT INTO `country` VALUES (null,   'Honduras');
INSERT INTO `country` VALUES (null,   'Hong Kong');
INSERT INTO `country` VALUES (null,   'Hungary');
INSERT INTO `country` VALUES (null,   'Iceland');
INSERT INTO `country` VALUES (null,   'India');
INSERT INTO `country` VALUES (null,   'Isle of Man');
INSERT INTO `country` VALUES (null,   'Indonesia');
INSERT INTO `country` VALUES (null,   'Iran (Islamic Republic of)');
INSERT INTO `country` VALUES (null,   'Iraq');
INSERT INTO `country` VALUES (null,   'Ireland');
INSERT INTO `country` VALUES (null,   'Israel');
INSERT INTO `country` VALUES (null,   'Italy');
INSERT INTO `country` VALUES (null,   'Ivory Coast');
INSERT INTO `country` VALUES (null,   'Jersey');
INSERT INTO `country` VALUES (null,   'Jamaica');
INSERT INTO `country` VALUES (null,   'Japan');
INSERT INTO `country` VALUES (null,   'Jordan');
INSERT INTO `country` VALUES (null,   'Kazakhstan');
INSERT INTO `country` VALUES (null,   'Kenya');
INSERT INTO `country` VALUES (null,   'Kiribati');
INSERT INTO `country` VALUES (null,   'Korea, Democratic People''s Republic of');
INSERT INTO `country` VALUES (null,   'Korea, Republic of');
INSERT INTO `country` VALUES (null,   'Kosovo');
INSERT INTO `country` VALUES (null,   'Kuwait');
INSERT INTO `country` VALUES (null,   'Kyrgyzstan');
INSERT INTO `country` VALUES (null,   'Lao People''s Democratic Republic');
INSERT INTO `country` VALUES (null,   'Latvia');
INSERT INTO `country` VALUES (null,   'Lebanon');
INSERT INTO `country` VALUES (null,   'Lesotho');
INSERT INTO `country` VALUES (null,   'Liberia');
INSERT INTO `country` VALUES (null,   'Libyan Arab Jamahiriya');
INSERT INTO `country` VALUES (null,   'Liechtenstein');
INSERT INTO `country` VALUES (null,   'Lithuania');
INSERT INTO `country` VALUES (null,   'Luxembourg');
INSERT INTO `country` VALUES (null,   'Macau');
INSERT INTO `country` VALUES (null,   'North Macedonia');
INSERT INTO `country` VALUES (null,   'Madagascar');
INSERT INTO `country` VALUES (null,   'Malawi');
INSERT INTO `country` VALUES (null,   'Malaysia');
INSERT INTO `country` VALUES (null,   'Maldives');
INSERT INTO `country` VALUES (null,   'Mali');
INSERT INTO `country` VALUES (null,   'Malta');
INSERT INTO `country` VALUES (null,   'Marshall Islands');
INSERT INTO `country` VALUES (null,   'Martinique');
INSERT INTO `country` VALUES (null,   'Mauritania');
INSERT INTO `country` VALUES (null,   'Mauritius');
INSERT INTO `country` VALUES (null,   'Mayotte');
INSERT INTO `country` VALUES (null,   'Mexico');
INSERT INTO `country` VALUES (null,   'Micronesia, Federated States of');
INSERT INTO `country` VALUES (null,   'Moldova, Republic of');
INSERT INTO `country` VALUES (null,   'Monaco');
INSERT INTO `country` VALUES (null,   'Mongolia');
INSERT INTO `country` VALUES (null,   'Montenegro');
INSERT INTO `country` VALUES (null,   'Montserrat');
INSERT INTO `country` VALUES (null,   'Morocco');
INSERT INTO `country` VALUES (null,   'Mozambique');
INSERT INTO `country` VALUES (null,   'Myanmar');
INSERT INTO `country` VALUES (null,   'Namibia');
INSERT INTO `country` VALUES (null,   'Nauru');
INSERT INTO `country` VALUES (null,   'Nepal');
INSERT INTO `country` VALUES (null,   'Netherlands');
INSERT INTO `country` VALUES (null,   'Netherlands Antilles');
INSERT INTO `country` VALUES (null,   'New Caledonia');
INSERT INTO `country` VALUES (null,   'New Zealand');
INSERT INTO `country` VALUES (null,   'Nicaragua');
INSERT INTO `country` VALUES (null,   'Niger');
INSERT INTO `country` VALUES (null,   'Nigeria');
INSERT INTO `country` VALUES (null,   'Niue');
INSERT INTO `country` VALUES (null,   'Norfolk Island');
INSERT INTO `country` VALUES (null,   'Northern Mariana Islands');
INSERT INTO `country` VALUES (null,   'Norway');
INSERT INTO `country` VALUES (null,   'Oman');
INSERT INTO `country` VALUES (null,   'Pakistan');
INSERT INTO `country` VALUES (null,   'Palau');
INSERT INTO `country` VALUES (null,   'Palestine');
INSERT INTO `country` VALUES (null,   'Panama');
INSERT INTO `country` VALUES (null,   'Papua New Guinea');
INSERT INTO `country` VALUES (null,   'Paraguay');
INSERT INTO `country` VALUES (null,   'Peru');
INSERT INTO `country` VALUES (null,   'Philippines');
INSERT INTO `country` VALUES (null,   'Pitcairn');
INSERT INTO `country` VALUES (null,   'Poland');
INSERT INTO `country` VALUES (null,   'Portugal');
INSERT INTO `country` VALUES (null,   'Puerto Rico');
INSERT INTO `country` VALUES (null,   'Qatar');
INSERT INTO `country` VALUES (null,   'Reunion');
INSERT INTO `country` VALUES (null,   'Romania');
INSERT INTO `country` VALUES (null,   'Russian Federation');
INSERT INTO `country` VALUES (null,   'Rwanda');
INSERT INTO `country` VALUES (null,   'Saint Kitts and Nevis');
INSERT INTO `country` VALUES (null,   'Saint Lucia');
INSERT INTO `country` VALUES (null,   'Saint Vincent and the Grenadines');
INSERT INTO `country` VALUES (null,   'Samoa');
INSERT INTO `country` VALUES (null,   'San Marino');
INSERT INTO `country` VALUES (null,   'Sao Tome and Principe');
INSERT INTO `country` VALUES (null,   'Saudi Arabia');
INSERT INTO `country` VALUES (null,   'Senegal');
INSERT INTO `country` VALUES (null,   'Serbia');
INSERT INTO `country` VALUES (null,   'Seychelles');
INSERT INTO `country` VALUES (null,   'Sierra Leone');
INSERT INTO `country` VALUES (null,   'Singapore');
INSERT INTO `country` VALUES (null,   'Slovakia');
INSERT INTO `country` VALUES (null,   'Slovenia');
INSERT INTO `country` VALUES (null,   'Solomon Islands');
INSERT INTO `country` VALUES (null,   'Somalia');
INSERT INTO `country` VALUES (null,   'South Africa');
INSERT INTO `country` VALUES (null,   'South Georgia South Sandwich Islands');
INSERT INTO `country` VALUES (null,   'South Sudan');
INSERT INTO `country` VALUES (null,   'Spain');
INSERT INTO `country` VALUES (null,   'Sri Lanka');
INSERT INTO `country` VALUES (null,   'St. Helena');
INSERT INTO `country` VALUES (null,   'St. Pierre and Miquelon');
INSERT INTO `country` VALUES (null,   'Sudan');
INSERT INTO `country` VALUES (null,   'Suriname');
INSERT INTO `country` VALUES (null,   'Svalbard and Jan Mayen Islands');
INSERT INTO `country` VALUES (null,   'Swaziland');
INSERT INTO `country` VALUES (null,   'Sweden');
INSERT INTO `country` VALUES (null,   'Switzerland');
INSERT INTO `country` VALUES (null,   'Syrian Arab Republic');
INSERT INTO `country` VALUES (null,   'Taiwan');
INSERT INTO `country` VALUES (null,   'Tajikistan');
INSERT INTO `country` VALUES (null,   'Tanzania, United Republic of');
INSERT INTO `country` VALUES (null,   'Thailand');
INSERT INTO `country` VALUES (null,   'Togo');
INSERT INTO `country` VALUES (null,   'Tokelau');
INSERT INTO `country` VALUES (null,   'Tonga');
INSERT INTO `country` VALUES (null,   'Trinidad and Tobago');
INSERT INTO `country` VALUES (null,   'Tunisia');
INSERT INTO `country` VALUES (null,   'Turkey');
INSERT INTO `country` VALUES (null,   'Turkmenistan');
INSERT INTO `country` VALUES (null,   'Turks and Caicos Islands');
INSERT INTO `country` VALUES (null,   'Tuvalu');
INSERT INTO `country` VALUES (null,   'Uganda');
INSERT INTO `country` VALUES (null,   'Ukraine');
INSERT INTO `country` VALUES (null,   'United Arab Emirates');
INSERT INTO `country` VALUES (null,   'United Kingdom');
INSERT INTO `country` VALUES (null,   'United States');
INSERT INTO `country` VALUES (null,   'United States minor outlying islands');
INSERT INTO `country` VALUES (null,   'Uruguay');
INSERT INTO `country` VALUES (null,   'Uzbekistan');
INSERT INTO `country` VALUES (null,   'Vanuatu');
INSERT INTO `country` VALUES (null,   'Vatican City State');
INSERT INTO `country` VALUES (null,   'Venezuela');
INSERT INTO `country` VALUES (null,   'Vietnam');
INSERT INTO `country` VALUES (null,   'Virgin Islands (British)');
INSERT INTO `country` VALUES (null,   'Virgin Islands (U.S.)');
INSERT INTO `country` VALUES (null,   'Wallis and Futuna Islands');
INSERT INTO `country` VALUES (null,   'Western Sahara');
INSERT INTO `country` VALUES (null,   'Yemen');
INSERT INTO `country` VALUES (null,   'Zambia');
INSERT INTO `country` VALUES (null,   'Zimbabwe');

INSERT INTO city VALUES (1, 'Copenhagen'),(2, 'Aarhus'),(3,'Odense'),(4,'Paris');


INSERT INTO zip VALUES (1,1000,1,57),(2,1300,1,57),(3,1600,1,57),(4,2200,1,57),(5,2100,1,57),(6,2400,1,57),(7,2840,4,73);

INSERT INTO address VALUES (1,'Kronprinsessegade',23,2,'th',2),(2,'Vesterbrogade',13,3,'tv',3),(3,'Leifsgade',3,2,'tv',1),(4,'Norrebrogade',23,2,'th',4),(5,'Jagtvej',13,3,'tv',4),(6,'Lygten',3,2,'tv',4),(7,'Vennemindevej',18,2,'th',5),(8,'Fanogade',14,2,'th',5),(9,'Arhusgade',20,1,'th',5),(10,'Thoravej',29,2,'tv',6),(11,'Norrebrogade',21,2,'tv',4),(12,'Sigurdsgade',28,2,'tv',4),(13,'Main',26,8,'-',7),(14,'Tower',8,9,'-',7),(15,'Sigurdsgade',28,2,'tv',4),(16,'Vesterbrogade',6,5,'th',3),(17,'Vesterbrogade',8,4,'tv',3);

INSERT INTO renter VALUES (1,'Lars','Larsen','121278-5689','ll@mail.com','004552814653','74920346734LK29382',9),(2,'Marc','Hansen','150675-8723','mc@mail.com','004534565423','749GM346734GS29382',10),(3,'Marie','Pedersen','230189-5432','mp@mail.com','004576904587','749GM346734GS20000',11),(4,'Lisa','Persen','150689-5432','lp@mail.com','004585688587','354TY786734GS21200',12),(5,'George','Johansen','ASDG812885455','gj@mail.com','004587592487','DSFR5746734GS20000',13),(6,'Nicole','Smith','SDFEGERSD489','ns@mail.com','005278754587','SDFE78346734GS20000',14),(7,'Lara','Hansen','080268-2232','lh@mail.com','004563596587','DF658726734GS20850',15),(8,'Per','Persen','121187-7437','pp@mail.com','004575789246','896RT346734GS20888',16),(9,'John','Petersen','250385-5433','jp@mail.com','004586778587','4897RT46734GS20000',17);



INSERT INTO brand VALUES (1,'Pössl'),(2,'Sunlight'),(3,'Carado');

INSERT INTO model VALUES (1,'Trenta',1,2,200),(2,'2Win PLUS',1,2,200),(3,'Cliff XV 640',2,2,300),(4,'V 66',2,4,400),(5,'A 68',2,6,800),(6,'T 67',2,5,500),(7,'T338',3,4,500),(8,'I338',3,4,600);

INSERT INTO vehicle VALUES (1,'CL65542',1,1,1),(2,'AI64442',1,1,1),(3,'JKG5H42',1,2,1),(4,'JI64F42',1,2,1),(5,'XM23640',1,3,2),(6,'CMO3640',1,3,2),(7,'XGH3640',1,4,2),(8,'CMO5540',1,4,2),(9,'PCH3640',1,4,2),(10,'GSG5540',1,4,2),(11,'BVG3640',1,4,2),(12,'PWI5540',1,4,2),(13,'CVS3640',1,4,2),(14,'VM23640',1,5,2),(15,'CM67540',1,5,2),(16,'KLI3640',1,5,2),(17,'POU7540',1,5,2),(18,'BBQ3640',1,5,2),(19,'PLQ7540',1,5,2),(20,'XRE3640',1,5,2),(21,'ZU38585',1,6,2),(22,'KLD8585',1,6,2),(23,'ZJI8585',1,6,2),(24,'ZXX9585',1,6,2),(25,'IIW8585',1,6,2),(26,'DDH8585',1,7,3),(27,'UYT8585',1,7,3),(28,'REN4685',1,7,3),(29,'ZU54685',1,7,3),(30,'KJH8585',1,7,3),(31,'OIO7540',1,8,3),(32,'TWR5540',1,8,3);

INSERT INTO agreement VALUES (1,1,1,'2020-03-20','2020-03-27',100,100,0,1,0),(2,3,1,'2020-04-02','2020-04-12',0,0,0,1,0),(3,2,3,'2020-04-02','2020-04-10',220,0,0,1,0),(4,null,8,'2020-06-12','2020-06-20',0,20,20,1,1),(5,2,null,'2020-08-02','2020-08-10',0,50,0,1,1),(6,null,null,'2020-09-02','2020-09-20',900,0,0,1,1),(7,8,10,'2020-06-08','2020-06-10',0,0,0,1,0),(8,8,7,'2020-01-12','2020-01-20',1220,120,50,1,0),(9,4,8,'2020-04-08','2020-04-10',220,80,0,1,0),(10,8,8,'2020-05-01','2020-05-06',0,500,200,0,1),(11,8,9,'2020-07-03','2020-07-18',0,0,0,1,0);



INSERT INTO job VALUES (1,'Sales Assistant'),(2,'Cleaning Staff'),(3,'Auto Mechanic'),(4,'Bookkeeper'),(5,'Owner');

INSERT INTO users (userID, username, password, role, enabled) VALUES ('1', 'kl', '$2a$10$JzKwGSOhwqndJCuAWoLE4eFqeb4Bex80KkON8cbX6POYytZAB4Q8e', 'user', '1');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('2', 'jp', '$2a$10$JmlPdA3K19VN1SXciGu5jeXr9K4FvZAaY2mwKg0zkyyN3C/FxKEza', 'user', '1');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('3', 'ma', '$2a$10$hW3/MbcQdTURGDDCLQCAIO1.W8nMEE.PVNBvArq0QpSgBStPZ0gDW', 'user', '1');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('4', 'aj', '$2a$10$jtdM7GWaGryWH9HUBn9rIeD9zJtTqIDCFqlKWNa1eAspaEXAbZ4rK', 'user', '1');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('5', null, null, 'restricted', '0');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('6', null, null, 'restricted', '0');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('7', null, null, 'restricted', '0');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('8', 'Morten', '$2a$10$Q0ddH3PNJXom.SEaFB87DejU07ReIz3hd7o.G9erunUwm6.yaMAwi', 'admin', '1');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('9', 'Andy', '$2a$10$HxD.ZJYra/qjbBlebLUHtOllDVhU4T5WQaSF6lMpFk.cwEmcPDto6', 'admin', '1');
INSERT INTO users (userID, username, password, role, enabled) VALUES ('10', 'Larry', '$2a$10$HxD.ZJYra/qjbBlebLUHtOllDVhU4T5WQaSF6lMpFk.cwEmcPDto6', 'admin', '1');

INSERT INTO employee VALUES (1,'Karl','Larsen','151278-5689','kl@mail.com','004564814653',1,1, 25000, 1),(2,'Jeanette','Pederson','150878-8723','jp@mail.com','004556565423',2,1,25000,2),
(3,'Marie','Andersen','240389-5432','ma@mail.com','004598545458',3,1,25000,3),(4,'Adam','Jensen','020578-5239','aj@mail.com','004556814656',4,1, 25000,4),
(5,'Agnes','Hansen','160772-8723','ah@mail.com','004567565467',5,2,20000,5),(6,'Daniel','Christensen','2509855432','dc@mail.com','004512125407',6,2,20000,6),
(7,'David','Rasmussen','060268-8123','dr@mail.com','004589924232',7,3,40000,7),(8,'Morten','Nielsen','131187-5082','mn@nordic.com','004552545458',8,4,35000,8),
(9,'Andy','Wachowski','131160-9082','aw@nordic.com','004552545498',9,5,200000,9),(10,'Larry','Wachowski','131160-7083','lw@nordic.com','004552345458',10,5,200000,10);

INSERT INTO extras VALUES (1, 'bike rack', 10),(2, 'bed linen', 10),(3, 'child seat', 20),(4, 'picnic table', 40),(5, 'chair', 5);

INSERT INTO agreement_has_extras VALUES (1, 1, 5),(2, 2, 6),(3, 4, 1);


