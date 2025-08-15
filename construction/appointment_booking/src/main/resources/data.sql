-- Doctors (Enhanced with more realistic data)
INSERT INTO doctors (name, email, phone, qualifications, average_rating, total_reviews, created_at)
VALUES
  ('Dr. Elena Rodriguez', 'elena.rodriguez@hospital.com', '(555) 001-0001', 'MD General Practice, Board Certified Family Medicine', 4.8, 127, CURRENT_TIMESTAMP()),
  ('Dr. Michael Chen', 'michael.chen@hospital.com', '(555) 002-0002', 'MD Pediatrics, Board Certified Pediatrician', 4.9, 98, CURRENT_TIMESTAMP()),
  ('Dr. Sarah Kim', 'sarah.kim@hospital.com', '(555) 003-0003', 'MD Dermatology, Board Certified Dermatologist', 4.7, 156, CURRENT_TIMESTAMP()),
  ('Dr. David Green', 'david.green@hospital.com', '(555) 004-0004', 'MD Cardiology, Board Certified Cardiologist', 4.6, 143, CURRENT_TIMESTAMP()),
  ('Dr. Emily White', 'emily.white@hospital.com', '(555) 005-0005', 'MD Ophthalmology, Board Certified Ophthalmologist', 4.9, 201, CURRENT_TIMESTAMP()),
  ('Dr. James Brown', 'james.brown@hospital.com', '(555) 006-0006', 'MD Orthopedics, Board Certified Orthopedic Surgeon', 4.3, 89, CURRENT_TIMESTAMP()),
  ('Dr. Olivia Perez', 'olivia.perez@hospital.com', '(555) 007-0007', 'MD Neurology, Board Certified Neurologist', 4.7, 134, CURRENT_TIMESTAMP()),
  ('Dr. Ben Carter', 'ben.carter@hospital.com', '(555) 008-0008', 'MD Psychiatry, Board Certified Psychiatrist', 4.8, 92, CURRENT_TIMESTAMP()),
  ('Dr. Sofia Garcia', 'sofia.garcia@hospital.com', '(555) 009-0009', 'DDS General Dentistry, Board Certified Dentist', 4.5, 187, CURRENT_TIMESTAMP()),
  ('Dr. John Smith', 'john.smith@hospital.com', '555-0101', 'MD Cardiology', 4.8, 120, CURRENT_TIMESTAMP()),
  ('Dr. Sarah Johnson', 'sarah.johnson@hospital.com', '555-0102', 'MD Dermatology', 4.6, 95, CURRENT_TIMESTAMP()),
  ('Dr. Mike Wilson', 'mike.wilson@hospital.com', '555-0103', 'MD Orthopedics', 4.7, 110, CURRENT_TIMESTAMP()),
  ('Dr. Lisa Brown', 'lisa.brown@hospital.com', '555-0104', 'MD Neurology', 4.5, 80, CURRENT_TIMESTAMP()),
  ('Dr. David Lee', 'david.lee@hospital.com', '555-0105', 'MD General Practice', 4.2, 60, CURRENT_TIMESTAMP());

-- Doctor specialties (Enhanced with more specialties)
-- Use FK doctor_id values starting at 1 due to identity generation
INSERT INTO doctor_specialties (id, doctor_id, specialty_type)
VALUES
  (101, 1, 'general'),
  (102, 2, 'pediatrics'),
  (103, 3, 'dermatology'),
  (104, 4, 'cardiology'),
  (105, 5, 'ophthalmology'),
  (106, 6, 'orthopedics'),
  (107, 7, 'neurology'),
  (108, 8, 'psychiatry'),
  (109, 9, 'dentistry'),
  (110, 10, 'cardiology'),
  (111, 11, 'dermatology'),
  (112, 12, 'orthopedics'),
  (113, 13, 'neurology'),
  (114, 14, 'general');

-- Doctor schedules (Enhanced with more comprehensive schedules)
INSERT INTO doctor_schedules (doctor_id, day_of_week, start_time, end_time, appointment_duration)
VALUES
  -- Dr. Elena Rodriguez (General Practice)
  (1, 'MONDAY', '09:00:00', '12:00:00', 30),
  (1, 'MONDAY', '14:00:00', '17:00:00', 30),
  (1, 'WEDNESDAY', '09:00:00', '12:00:00', 30),
  (1, 'FRIDAY', '10:00:00', '15:00:00', 30),
  
  -- Dr. Michael Chen (Pediatrics)
  (2, 'TUESDAY', '08:00:00', '12:00:00', 30),
  (2, 'TUESDAY', '13:00:00', '17:00:00', 30),
  (2, 'THURSDAY', '09:00:00', '13:00:00', 30),
  (2, 'SATURDAY', '09:00:00', '12:00:00', 30),
  
  -- Dr. Sarah Kim (Dermatology)
  (3, 'MONDAY', '10:00:00', '14:00:00', 45),
  (3, 'WEDNESDAY', '13:00:00', '17:00:00', 45),
  (3, 'FRIDAY', '09:00:00', '13:00:00', 45),
  
  -- Dr. David Green (Cardiology)
  (4, 'MONDAY', '08:00:00', '12:00:00', 60),
  (4, 'TUESDAY', '14:00:00', '18:00:00', 60),
  (4, 'THURSDAY', '08:00:00', '12:00:00', 60),
  
  -- Dr. Emily White (Ophthalmology)
  (5, 'TUESDAY', '09:00:00', '13:00:00', 30),
  (5, 'WEDNESDAY', '14:00:00', '18:00:00', 30),
  (5, 'FRIDAY', '08:00:00', '12:00:00', 30);

-- Symptom analyses
INSERT INTO symptom_analyses (patient_id, description, keywords, analyzed_at)
VALUES
  (1001, 'Chest pain and shortness of breath during exercise', NULL, CURRENT_TIMESTAMP()),
  (1002, 'Persistent skin rash with itching', NULL, CURRENT_TIMESTAMP());

-- Recommendations for analyses
-- H2 identity starts at 1; first two rows above become IDs 1 and 2
INSERT INTO recommendations (analysis_id, specialty, confidence, suggested_doctor_ids)
VALUES
  (1, 'cardiology', 0.95, '1'),
  (2, 'dermatology', 0.92, '2');

-- Appointments (Enhanced with more comprehensive appointment data)
INSERT INTO appointments (patient_id, doctor_id, appointment_date_time, duration_minutes, notes, status, time_zone, created_at)
VALUES
  -- Upcoming appointments
  (1001, 1, TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP()), 30, 'Annual physical examination', 'CONFIRMED', 'UTC', CURRENT_TIMESTAMP()),
  (1001, 3, TIMESTAMPADD(DAY, 3, CURRENT_TIMESTAMP()), 45, 'Skin mole check-up', 'PENDING', 'UTC', CURRENT_TIMESTAMP()),
  (1001, 5, TIMESTAMPADD(DAY, 7, CURRENT_TIMESTAMP()), 30, 'Eye examination', 'CONFIRMED', 'UTC', CURRENT_TIMESTAMP()),
  (1002, 2, TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP()), 30, 'Child wellness visit', 'CONFIRMED', 'UTC', CURRENT_TIMESTAMP()),
  (1002, 4, TIMESTAMPADD(DAY, 5, CURRENT_TIMESTAMP()), 60, 'Cardiology consultation', 'PENDING', 'UTC', CURRENT_TIMESTAMP()),
  
  -- Past appointments  
  (1001, 1, TIMESTAMPADD(DAY, -30, CURRENT_TIMESTAMP()), 30, 'Follow-up consultation', 'COMPLETED', 'UTC', TIMESTAMPADD(DAY, -35, CURRENT_TIMESTAMP())),
  (1001, 4, TIMESTAMPADD(DAY, -45, CURRENT_TIMESTAMP()), 60, 'Heart health screening', 'COMPLETED', 'UTC', TIMESTAMPADD(DAY, -50, CURRENT_TIMESTAMP())),
  (1002, 3, TIMESTAMPADD(DAY, -20, CURRENT_TIMESTAMP()), 45, 'Acne treatment', 'CANCELLED', 'UTC', TIMESTAMPADD(DAY, -25, CURRENT_TIMESTAMP())),
  (1002, 1, TIMESTAMPADD(DAY, -60, CURRENT_TIMESTAMP()), 30, 'General check-up', 'COMPLETED', 'UTC', TIMESTAMPADD(DAY, -65, CURRENT_TIMESTAMP()));

-- Doctor ratings (Enhanced with more comprehensive rating data)
INSERT INTO doctor_ratings (doctor_id, patient_id, stars, feedback)
VALUES
  (1, 1001, 5, 'Dr. Rodriguez was extremely thorough and caring. Highly recommend!'),
  (1, 1002, 4, 'Good consultation, very professional and knowledgeable.'),
  (2, 1001, 5, 'Dr. Chen is amazing with kids. My child felt comfortable throughout.'),
  (3, 1002, 4, 'Dr. Kim provided excellent skin care advice and treatment options.'),
  (4, 1001, 5, 'Dr. Green saved my life with early heart disease detection. Grateful!'),
  (5, 1002, 4, 'Professional eye exam with detailed explanation of results.'),
  (1, 1003, 5, 'Best family doctor I''ve ever had. Always available and caring.'),
  (2, 1004, 4, 'Great pediatrician, very patient with nervous children.'),
  (3, 1001, 3, 'Decent dermatologist but wait times can be long.'),
  (4, 1005, 5, 'Dr. Green''s expertise in cardiology is exceptional. Very thorough.'),
  (5, 1003, 4, 'Quick and efficient eye examination with clear recommendations.');


