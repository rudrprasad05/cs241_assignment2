-- Start transaction
START TRANSACTION;

-- Step 1: Insert into the users table
INSERT INTO user (role, email, f_name, l_name, password, username)
VALUES ('ADMIN', 'a@example.com', 'Admin', 'User', '$2a$10$9y/KRb.2j3XjjwG79McdLOVKqpfmURWaqOTd29YjMBMj6lrfsflmu', 'admin');

-- Step 2: Insert into the admin table using the last inserted ID from the users table
INSERT INTO admin (id, admin_level)
VALUES (LAST_INSERT_ID(), 0);

INSERT INTO invite_link (invite_code, is_redeemed, user_id) VALUES ('2f57d3c75e794b70a2c10b34adfc01612f57d3c75e794b70a2c10b34adfc0161', 1, LAST_INSERT_ID());

INSERT INTO period (time) VALUES ('08:00:00'), ('09:00:00'), ('11:00:00'), ('12:00:00'), ('14:00:00'), ('15:00:00');

-- Commit the transaction if all goes well
COMMIT;