-- Doctors
INSERT INTO doctors (name, email, phone, qualifications, average_rating, total_reviews, created_at)
VALUES
  ('Dr. John Smith', 'john.smith@hospital.com', '555-0101', 'MD Cardiology', 4.8, 120, CURRENT_TIMESTAMP()),
  ('Dr. Sarah Johnson', 'sarah.johnson@hospital.com', '555-0102', 'MD Dermatology', 4.6, 95, CURRENT_TIMESTAMP()),
  ('Dr. Mike Wilson', 'mike.wilson@hospital.com', '555-0103', 'MD Orthopedics', 4.7, 110, CURRENT_TIMESTAMP()),
  ('Dr. Lisa Brown', 'lisa.brown@hospital.com', '555-0104', 'MD Neurology', 4.5, 80, CURRENT_TIMESTAMP()),
  ('Dr. David Lee', 'david.lee@hospital.com', '555-0105', 'MD General Practice', 4.2, 60, CURRENT_TIMESTAMP());

-- Doctor specialties
-- Use FK doctor_id values starting at 1 due to identity generation
INSERT INTO doctor_specialties (id, doctor_id, specialty_type)
VALUES
  (101, 1, 'cardiology'),
  (102, 2, 'dermatology'),
  (103, 3, 'orthopedics'),
  (104, 4, 'neurology'),
  (105, 5, 'general');

-- Doctor schedules
INSERT INTO doctor_schedules (doctor_id, day_of_week, start_time, end_time, appointment_duration)
VALUES
  (1, 'MONDAY', '09:00:00', '12:00:00', 30),
  (1, 'WEDNESDAY', '14:00:00', '17:00:00', 30),
  (2, 'TUESDAY', '10:00:00', '13:00:00', 30);

-- Symptom analyses
INSERT INTO symptom_analyses (patient_id, description, keywords, analyzed_at)
VALUES
  (1001, 'Chest pain and shortness of breath during exercise', NULL, CURRENT_TIMESTAMP()),
  (1002, 'Persistent skin rash with itching', NULL, CURRENT_TIMESTAMP());

-- Recommendations for analyses
-- H2 identity starts at 1; first two rows above become IDs 1 and 2
INSERT INTO recommendations (analysis_id, specialty)
VALUES
  (1, 'cardiology'),
  (2, 'dermatology');

-- Appointments
INSERT INTO appointments (patient_id, doctor_id, appointment_date_time, duration_minutes, notes, status, time_zone, created_at)
VALUES
  (1001, 1, TIMESTAMPADD(MINUTE, 60, CURRENT_TIMESTAMP()), 30, 'Initial consultation', 'PENDING', 'UTC', CURRENT_TIMESTAMP()),
  (1002, 2, TIMESTAMPADD(MINUTE, 120, CURRENT_TIMESTAMP()), 30, 'Skin check-up', 'CONFIRMED', 'UTC', CURRENT_TIMESTAMP());

-- Doctor ratings (optional sample)
INSERT INTO doctor_ratings (doctor_id, patient_id, stars, feedback)
VALUES
  (1, 1001, 5, 'Excellent consultation'),
  (2, 1002, 4, 'Helpful advice');


