-- Set up database:

CREATE DATABASE teachersFirst;
USE teachersFirst;

-- Default Admin Account (be sure to change the password 'abcDEF123'):
CREATE USER 'teachersFirst'@'%' IDENTIFIED BY 'abcDEF123';
GRANT ALL PRIVILEGES ON *.* to 'teachersFirst'@'%' WITH GRANT OPTION;


CREATE TABLE keepalive (
    recID               INT               NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (recID)
);
INSERT INTO keepalive VALUES ();


CREATE TABLE members (
    recID               INT               NOT NULL AUTO_INCREMENT,
    loginName           VARCHAR(40)       NOT NULL,
    passwordHash        CHAR(40)          NOT NULL,
    displayName         VARCHAR(60)       NOT NULL,
    birthdate           DATE              NOT NULL,
    gender              VARCHAR(1)        NOT NULL,
    instructorNotes     VARCHAR(800)      NOT NULL,
    phone1              VARCHAR(20)       NOT NULL,
    phone2              VARCHAR(20)       NOT NULL,
    email               VARCHAR(40)       NOT NULL,
    isStudent           TINYINT(1)        NOT NULL DEFAULT 0,
    isInstructor        TINYINT(1)        NOT NULL DEFAULT 0,
    isAdmin             TINYINT(1)        NOT NULL DEFAULT 0,
    PRIMARY KEY (recID),
    CONSTRAINT loginName_UNIQUE UNIQUE (loginName)
);


CREATE TABLE appointments (
    recID               INT               NOT NULL AUTO_INCREMENT,
    studentID           INT               NOT NULL,
    instructorID        INT               NOT NULL,
    startTime           DATETIME          NOT NULL,
    endTime             DATETIME          NOT NULL,
    PRIMARY KEY (recID)
);


CREATE TABLE openings (
    recID               INT               NOT NULL AUTO_INCREMENT,
    instructorID        INT               NOT NULL,
    startTime           DATETIME          NOT NULL,
    endTime             DATETIME          NOT NULL,
    PRIMARY KEY (recID)
);


CREATE TABLE services (
    recID               INT               NOT NULL AUTO_INCREMENT,
    name                VARCHAR(45)       NOT NULL,
    description         VARCHAR(200)      NOT NULL,
    instructors         VARCHAR(45)       NOT NULL,
    PRIMARY KEY (recID)
);


CREATE TABLE loggedEvents (
    recID               INT               NOT NULL AUTO_INCREMENT,
    operator            INT               NOT NULL,
    date                DATETIME          NOT NULL,
    message             VARCHAR(200)      NOT NULL,
    PRIMARY KEY (recID)
);


-- Useful Snippets:

-- Create new user (Note: last three number 1-0-0 are student/instructor/admin):
INSERT INTO members (loginName, passwordHash, displayName, credits, birthdate, gender, instructorNotes, phone1, email, isStudent, isInstructor, isAdmin) VALUES ('studentLogin', 'password', 'Student Name', 0, '1995-05-25', 'm', 'Notes about student.', '444-444-4444', 'abc@xyz.com', 1, 0, 0);
INSERT INTO members (loginName, passwordHash, displayName, credits, birthdate, gender, instructorNotes, phone1, email, isStudent, isInstructor, isAdmin) VALUES ('instructorLogin', 'password', 'Instructor Name', 0, '1995-05-25', 'm', 'Instructor introduction.', '444-444-4444', 'abc@xyz.com', 0, 1, 0);
INSERT INTO members (loginName, passwordHash, displayName, credits, birthdate, gender, instructorNotes, phone1, email, isStudent, isInstructor, isAdmin) VALUES ('adminLogin', 'password', 'Admin Name', 0, '1995-05-25', 'm', 'No description needed.', '444-444-4444', 'abc@xyz.com', 0, 0, 1);

-- Change user password:
UPDATE members SET passwordHash = SHA1('password') WHERE (id = 1);

