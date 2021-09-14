-- Set up database:

CREATE SCHEMA teachersFirst;
USE teachersFirst;

-- Default Admin Account (be sure to change the password 'abcDEF123'):
CREATE USER 'teachersFirst'@'%' IDENTIFIED BY 'abcDEF123';
GRANT ALL PRIVILEGES ON *.* to 'teachersFirst'@'%' WITH GRANT OPTION;


-- Tables:

CREATE TABLE teachersFirst.members (
    recID               INT(11)           NOT NULL AUTO_INCREMENT,
    loginName           VARCHAR(40)       NOT NULL,
    passwordHash        CHAR(40)          DEFAULT NULL,
    token               CHAR(40)          DEFAULT NULL,
    displayName         VARCHAR(60)       NOT NULL,
    credits             FLOAT             NOT NULL DEFAULT 0,
    birthdate           DATE              NOT NULL DEFAULT '1800-01-01',
    gender              VARCHAR(1)        NOT NULL DEFAULT '',
    selfIntroduction    VARCHAR(800)      NOT NULL DEFAULT '',
    instructorNotes     VARCHAR(800)      NOT NULL DEFAULT '',
    phone1              VARCHAR(20)       NOT NULL DEFAULT '',
    phone2              VARCHAR(20)       NOT NULL DEFAULT '',
    email               VARCHAR(40)       NOT NULL DEFAULT '',
    isAdmin             TINYINT(1)        NOT NULL DEFAULT 0,
    isInstructor        TINYINT(1)        NOT NULL DEFAULT 0,
    isStudent           TINYINT(1)        NOT NULL DEFAULT 0,
    isDeleted           TINYINT(1)        NOT NULL DEFAULT 0,
    PRIMARY KEY (recID),
    UNIQUE KEY loginName_UNIQUE (loginName)
);


CREATE TABLE teachersFirst.appointments (
    recID               INT(11)           NOT NULL AUTO_INCREMENT,
    studentID           INT(11)           NOT NULL,
    instructorID        INT(11)           NOT NULL,
    startTime           DATETIME          NOT NULL,
    endTime             DATETIME          NOT NULL,
    schedulingVerified  TINYINT(1)        NOT NULL DEFAULT 0,
    completionState     TINYINT(1)        NOT NULL DEFAULT -1,
    PRIMARY KEY (recID)
);


CREATE TABLE teachersFirst.openings (
    recID               INT(11)           NOT NULL AUTO_INCREMENT,
    instructorID        INT(11)           NOT NULL,
    startTime           DATETIME          NOT NULL,
    endTime             DATETIME          NOT NULL,
    PRIMARY KEY (recID)
);


CREATE TABLE teachersFirst.loggedEvents (
    recID               INT(11)           NOT NULL AUTO_INCREMENT,
    operator            INT(11)           NOT NULL,
    date                DATETIME          NOT NULL,
    message             VARCHAR(200)      NOT NULL,
    PRIMARY KEY (recID)
);


-- Useful Snippets:

-- Create new user (Note: last three number 1-0-0 are student/instructor/admin):
INSERT INTO teachersFirst.members (loginName, passwordHash, displayName, birthdate, instructorNotes, isAdmin, isInstructor, isStudent) VALUES ('superuser', SHA1('password'), 'Superuser', '1800-01-01', 'This user has authority to perform administrative actions and cannot be deleted.', 1, 0, 0);

INSERT INTO teachersFirst.members (loginName, passwordHash, displayName, credits, birthdate, gender, phone1, email, isAdmin, isInstructor, isStudent) VALUES ('adminLogin', SHA1('password'), 'Admin Name', 0, '1995-08-12', 'm', '444-444-4444', 'abc@xyz.com', 1, 0, 0);
INSERT INTO teachersFirst.members (loginName, passwordHash, displayName, credits, birthdate, gender, selfIntroduction, phone1, email, isAdmin, isInstructor, isStudent) VALUES ('instructorLogin', SHA1('password'), 'Instructor Name', 0, '1985-03-24', 'm', 'Instructor introduction.', '444-444-4444', 'abc@xyz.com', 0, 1, 0);
INSERT INTO teachersFirst.members (loginName, passwordHash, displayName, credits, birthdate, gender, instructorNotes, phone1, email, isAdmin, isInstructor, isStudent) VALUES ('studentLogin', SHA1('password'), 'Student Name', 0, '1990-05-08', 'm', 'Notes about student.', '444-444-4444', 'abc@xyz.com', 0, 0, 1);

-- Change user password:
UPDATE teachersFirst.members SET passwordHash = SHA1('password') WHERE (id = 1);

