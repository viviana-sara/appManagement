CREATE SCHEMA if not exists `OrderManagement` ;

use OrderManagement;

CREATE TABLE `OrderManagement`.`Products` (
  `idProducts` INT NOT NULL,
  `nameProduct` VARCHAR(45) NULL,
  `quantity` INT NULL,
  `price` VARCHAR(45) NULL,
  PRIMARY KEY (`idProducts`));

CREATE TABLE `OrderManagement`.`Client` (
  `idClient` INT NOT NULL,
  `nameClient` VARCHAR(100) NULL,
  `address` VARCHAR(100) NULL,
  PRIMARY KEY (`idClient`));
  
  CREATE TABLE `OrderManagement`.`Order` (
  `idOrder` INT NOT NULL,
  `idOrderClient` INT NULL,
  `idOrderProduct` INT NULL,
  `quantityOrder` INT NULL,
  PRIMARY KEY (`idOrder`));
  


