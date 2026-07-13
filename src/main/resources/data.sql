INSERT IGNORE INTO authorized_students
(college_id, first_name, last_name, department, year, claimed, claimed_by_email)
VALUES
('ENTC2026001', 'Tanushri', 'Matre', 'ENTC', 3, false, NULL),
('ENTC2026002', 'Rahul', 'Sharma', 'ENTC', 2, false, NULL),
('COMP2026001', 'Sneha', 'Patil', 'Computer', 3, false, NULL),
('IT2026001', 'Amit', 'Joshi', 'Information Technology', 4, false, NULL),
('MECH2026001', 'Rohan', 'Deshmukh', 'Mechanical', 2, false, NULL),
('CIVIL2026001', 'Priya', 'Kulkarni', 'Civil', 1, false, NULL),
('AIDS2026001', 'Neha', 'Jadhav', 'AI & Data Science', 3, false, NULL),
('ECE2026001', 'Karan', 'More', 'Electronics', 4, false, NULL),
('CHEM2026001', 'Sakshi', 'Pawar', 'Chemical', 2, false, NULL),
('MBA2026001', 'Aditya', 'Patel', 'MBA', 1, false, NULL);

INSERT IGNORE INTO admins (email, password, category)
VALUES
('mainadmin@campuscare.com', '$2a$10$oAHZnqngseAooWiP.idwPujoi2ejGE6JPsOS9XaPXc9kOuCsXz9HK', 'MAIN'),
('hostel@campuscare.com', '$2a$10$l3F/a/P5qyOxczkxhLZbfeZtNN5677aVJR2WkuFHU11K.QZA7.e2O', 'HOSTEL'),
('mess@campuscare.com', '$2a$10$s.B7yzlbC.d20m8EimETq.cOr95frghUG7MrKYnHJjgqsAs0QDqMC', 'MESS'),
('academics@campuscare.com', '$2a$10$EkTzPwzdgPscaRvTVUFm2u5Oh1OdzdCvj2Nl/LPRXWmkmSudZ720.', 'ACADEMICS'),
('bus@campuscare.com', '$2a$10$Bd9dK56vTJhKDyL1qp00yuFDmIKfMqaqFhNZtAHkftW2VndrybI9S', 'BUS'),
('campus@campuscare.com', '$2a$10$802uF2WnOmhMpDHnufdqbe5zYC5BLm36jr3XJqVChtIDlrejayaI6', 'CAMPUS');