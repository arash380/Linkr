CREATE DATABASE IF NOT EXISTS linkrDatabase;


CREATE USER IF NOT EXISTS 'team2'@'localhost' IDENTIFIED BY 'pass';
CREATE USER IF NOT EXISTS 'team2'@'%' IDENTIFIED BY 'pass';
GRANT ALL ON linkrDatabase.* TO 'team2'@'localhost';
GRANT ALL ON linkrDatabase.* TO 'team2'@'%';


USE linkrDatabase;


DROP TABLE IF EXISTS timesheet_row;
DROP TABLE IF EXISTS timesheet;
DROP TABLE IF EXISTS rate_sheet;
DROP TABLE IF EXISTS user_credential;
DROP TABLE IF EXISTS project_employee;
DROP TABLE IF EXISTS employee_work_package;
DROP TABLE IF EXISTS work_package_estimate_cost;
DROP TABLE IF EXISTS project_work_package_estimate;
DROP TABLE IF EXISTS work_package_estimate_to_completion;
DROP TABLE IF EXISTS work_package;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS employee;




CREATE TABLE employee (
        ID INT NOT NULL,
        firstName VARCHAR(50) NOT NULL,
        lastName VARCHAR(50) NOT NULL,
        vacationDays FLOAT,
        sickDays FLOAT,
        flexTime  FLOAT,
        hrEmployee BOOLEAN,
        payRate VARCHAR(2),
        supervisor_ID INT,
        approver_ID INT,
        salariedEmployee BOOLEAN,
        isActive BOOLEAN,
        PRIMARY KEY (ID)
);


INSERT INTO employee VALUES(111, "Joe", "Smith", 0, 0, 0, 1, "P1", 112,   112, 1, 1);
INSERT INTO employee VALUES(112, "Mary", "Smith", 0, 0, 0, 1, "P1", NULL, NULL, 1, 1);


CREATE TABLE user_credential (
        employee_ID INT NOT NULL,
        userName VARCHAR(10) NOT NULL UNIQUE,
        password VARCHAR(64) NOT NULL,
        salt VARCHAR(64) NOT NULL,
        PRIMARY KEY (employee_ID),
        FOREIGN KEY (employee_ID) REFERENCES employee(ID)
);


INSERT INTO user_credential VALUES(111, "SmithJ", "30c353964b5e951ef1c6bc027b70e96bbf6c1c99655187b076060983132dfa4f", "4a6d65498cdacb1aaa0619fab91128c9eac4ba90c13c67f8b2c012ce444620d1");


CREATE TABLE timesheet (
        ID INT NOT NULL auto_increment,
        endDate DATE NOT NULL,
        versionNumber INT NOT NULL,
        employee_ID INT NOT NULL,
        managerApproval BOOLEAN,
        employeeSignature VARCHAR(100),        
        PRIMARY KEY (ID),
        FOREIGN KEY (employee_ID) REFERENCES employee(ID)
);


INSERT INTO timesheet VALUES(1, "2022-01-07", 1, 111, 0, NULL);


CREATE TABLE project (
        ID VARCHAR(4) NOT NULL,
        employee_ID INT,
        projectManagerAssistant_ID INT,
        activeProject BOOL,
        projectName VARCHAR(30),
        projectStart DATE,
        projectEnd DATE,
        projectBudget FLOAT,
        projectMarkup FLOAT,
        unallocatedBudget FLOAT,
        PRIMARY KEY (ID),
        FOREIGN KEY (employee_ID) REFERENCES employee(ID),
    FOREIGN KEY (projectManagerAssistant_ID) REFERENCES employee(ID)
);                




INSERT INTO project VALUES("1232", 112, 111, 1, "Test Project", '2022-01-30', '2022-12-30', 500000, 1.75, 500000);        


CREATE TABLE rate_sheet (
        labourRateName VARCHAR(2) NOT NULL,
        year INT NOT NULL,
        labourRate FLOAT,
        PRIMARY KEY (labourRateName, year)
);


INSERT INTO rate_sheet VALUES ("P1", 2022, 100.5);
INSERT INTO rate_sheet VALUES ("P2", 2022, 140);
INSERT INTO rate_sheet VALUES ("P3", 2022, 180.5);
INSERT INTO rate_sheet VALUES ("P4", 2022, 220);
INSERT INTO rate_sheet VALUES ("P5", 2022, 260.5);
INSERT INTO rate_sheet VALUES ("JS", 2022, 65);
INSERT INTO rate_sheet VALUES ("SS", 2022, 95);
INSERT INTO rate_sheet VALUES ("DS", 2022, 45);


CREATE TABLE project_employee (
        employee_ID INT NOT NULL,
        project_ID VARCHAR(4) NOT NULL,
        PRIMARY KEY(employee_ID, project_ID),
        FOREIGN KEY (employee_ID) REFERENCES employee(ID),
        FOREIGN KEY (project_ID) REFERENCES project(ID)
);


INSERT INTO project_employee VALUES(111, "1232");        


CREATE TABLE work_package (
        ID VARCHAR(5) NOT NULL,
        project_ID VARCHAR(4) NOT NULL,
        responsibleEngineer_ID INT,
        workPackageTitle VARCHAR(30),
        unallocatedBudget FLOAT,
        startingBudget FLOAT,
        chargable BOOL,
        workpackageStartDate DATE,
        workpackageEndDate DATE,
        completed BOOL,
        PRIMARY KEY (ID, project_ID),
        FOREIGN KEY (project_ID) REFERENCES project(ID),
        FOREIGN KEY (responsibleEngineer_ID) REFERENCES employee(ID)
);


INSERT INTO work_package VALUES("A0000", "1232", 112, "Test Package", 125000, 125000, 0, '2022-01-31', '2022-03-12', 0);
INSERT INTO work_package VALUES("30000", "1232", 112, "Test Package", 125000, 125000, 0, '2022-01-31', '2022-03-12', 0);
INSERT INTO work_package VALUES("A1000", "1232", 112, "Test Package", 125000, 125000, 0, '2022-01-31', '2022-03-12', 0);
INSERT INTO work_package VALUES("A1100", "1232", 112, "Test Package", 125000, 125000, 1, '2022-01-31', '2022-03-12', 0);
INSERT INTO work_package VALUES("AG000", "1232", 112, "Test Package", 125000, 125000, 1, '2022-01-31', '2022-03-12', 0);
INSERT INTO work_package VALUES("31000", "1232", 112, "Test Package", 125000, 125000, 1, '2022-01-31', '2022-03-12', 0);


CREATE TABLE employee_work_package (
        workPackage_ID VARCHAR(5) NOT NULL,
        workPackage_project_ID VARCHAR(4) NOT NULL,
        employee_ID INT NOT NULL,
        PRIMARY KEY (workPackage_ID, workPackage_project_ID, employee_ID),
        FOREIGN KEY (workPackage_ID, workPackage_project_ID) REFERENCES work_package(ID, project_ID),
        FOREIGN KEY (employee_ID) REFERENCES employee(ID)
);


INSERT INTO employee_work_package VALUES ("A1100", "1232", 111);


CREATE TABLE work_package_estimate_cost (
        workPackage_ID VARCHAR(5) NOT NULL,
        workPackage_project_ID VARCHAR(4) NOT NULL,
        p1PlannedDays FLOAT,
        p2PlannedDays FLOAT,
        p3PlannedDays FLOAT,
        p4PlannedDays FLOAT,
        p5PlannedDays FLOAT,
        jsPlannedDays FLOAT,
        ssPlannedDays FLOAT,
        dsPlannedDays FLOAT,
        PRIMARY KEY (workPackage_ID, workPackage_project_ID),
        FOREIGN KEY (workPackage_ID, workPackage_project_ID) REFERENCES work_package(ID, project_ID)
);


INSERT INTO work_package_estimate_cost VALUES("A1100", "1232", 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8);


CREATE TABLE project_work_package_estimate (
        workPackage_ID VARCHAR(5) NOT NULL,
        workPackage_project_ID VARCHAR(4) NOT NULL,
        p1PlannedDays FLOAT,
        p2PlannedDays FLOAT,
        p3PlannedDays FLOAT,
        p4PlannedDays FLOAT,
        p5PlannedDays FLOAT,
        jsPlannedDays FLOAT,
        ssPlannedDays FLOAT,
        dsPlannedDays FLOAT,
        PRIMARY KEY (workPackage_ID, workPackage_project_ID),
        FOREIGN KEY (workPackage_ID, workPackage_project_ID) REFERENCES work_package(ID, project_ID)
);


INSERT INTO project_work_package_estimate VALUES("A1100", "1232", 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8);


CREATE TABLE work_package_estimate_to_completion (
        workPackage_ID VARCHAR(5) NOT NULL,
        workPackage_project_ID VARCHAR(4) NOT NULL,
        p1PlannedDays FLOAT,
        p2PlannedDays FLOAT,
        p3PlannedDays FLOAT,
        p4PlannedDays FLOAT,
        p5PlannedDays FLOAT,
        jsPlannedDays FLOAT,
        ssPlannedDays FLOAT,
        dsPlannedDays FLOAT,
    percentageComplete INT,
        PRIMARY KEY (workPackage_ID, workPackage_project_ID),
        FOREIGN KEY (workPackage_ID, workPackage_project_ID) REFERENCES work_package(ID, project_ID)
);


INSERT INTO work_package_estimate_to_completion VALUES("A1100", "1232", 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 87);


CREATE TABLE timesheet_row (
        timesheet_ID INT NOT NULL,
        workPackage_project_ID VARCHAR(4) NOT NULL,
        workPackage_ID VARCHAR(5) NOT NULL,
        hoursWorked BIGINT,
        PRIMARY KEY (timesheet_ID, workPackage_ID, workPackage_project_ID),
        FOREIGN KEY (timesheet_ID) REFERENCES timesheet(ID),
        FOREIGN KEY (workpackage_ID, workPackage_project_ID) REFERENCES work_package(ID, project_ID)
);


INSERT INTO timesheet_row VALUES(1, "1232", "A1100", 22021235314409522);