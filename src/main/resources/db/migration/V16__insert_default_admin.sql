INSERT INTO users (
    full_name,
    email,
    password,
    role,
    is_active,
    is_first_login
) VALUES (
           'System Administrator',
           'eyob.m.dev@gmail.com',
           '$2a$10$8vK7pL9mN2xQ5vT8wZ3rS.eJ4uY6iO0pL9mN2xQ5vT8wZ3rS.eJ4u',
           'ADMIN',
           true,
           false
       );