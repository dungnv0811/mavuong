# AIDLC demo prompts

## Phase 1: Inception

### Step 1.1:

Your Role: You are an expert product manager and are tasked with creating well defined user stories that becomes the contract for developing the system as mentioned in the Task section below.

Plan for the work ahead and write your steps in an md file (plan.md) with checkboxes for each step in the plan. If any step needs my clarification, add a note in the step to get my confirmation. Do not make critical decisions on your own.
Upon completing the plan, ask for my review and approval. After my approval, you can go ahead to execute the plan one step at a time. Once you finish each step, mark the checkboxes as done in the plan.

Your Task: I would like to build a web application for scheduling health check-up appointments. There are 3 roles in the app: Patient, Doctor and Admin. These are the core functions system would have:
Firstly, the doctor and patients can register their profiles in the app and manage their profiles. They can log in to the app through their credentials (email/password).
Then, the app allows patients to find doctors through the available doctor list, book and manage for medical visits with private doctors.
The app enables patients to book appointments by selecting doctors, date time for appointment, take note for doctor. Auto-generated slots, prevent double-booking, time-zone handling.
Also, the app allows patients view and modify existing booking and management of medical/health check-up appointments list. Patients can cancel their appointments.
Patients can view their appointment history list and reschedule the previous appointment if needed.
Doctors and patients receive automated reminders for upcoming visits via email or sms.
For doctors, they can management of medical/health check-up appointments, view profiles of their patients. Doctors can manual confirm bookings in 1 or 2 hours.
Doctor also can set up their schedule, working time, day offs, holiday. Patients can only book the appointments based on the available time of doctor.
Build a basic dashboard to display the upcoming appointments for doctors and patients view. Also, the health insights/suggestions based on the healthcheck of patients and the appointment with doctors in any specialties.
In addition, the app will allow patients to set the ratings and give feedbacks for their doctors.
Base on the data system record for ratings and feedbacks from patients, system will generate smart matching: suggest best-fit high-rated doctors and push the suggestions the high-rated doctors to patients based on their needs.

Create an /inception/ directory and write the user stories to overview_user_stories.md in the inception directory. Only foucs on user stories and nothing else.
### Step 1.2:

Your Role: You are an expert software architect and are tasked with grouping the user stories into multiple units that can be built independently as mentioned in the Task section below.

Plan for the work ahead and write your steps in an md file (plan.md) with checkboxes for each step in the plan. If any step needs my clarification, add a note in the step to get my confirmation. Do not make critical decisions on your own.
Upon completing the plan, ask for my review and approval. After my approval, you can go ahead to execute the same plan one step at a time. Once you finish each step, mark the checkboxes as done in the plan.

Your Task: Refer to the user stories in, /inception/overview_user_stories.md file. Group the user stories into multiple units that can be built independently. Each unit contains highly cohesive user stories that can be built by a single team.
The units must be loosely coupled with each other. For each unit, write their respective user stories and acceptance criteria in individual .md files in the /inception/units/ folder. Do not start the technical systems design yet.

## Phase 2: Construction of one Unit

### Step 2.1:

Your Role: You are an expert software architect and are tasked with designing the domain model using Domain Driven Design for a unit of of the software system. Refer to the Task section for more details.

Plan for the work ahead and write your steps in an md file (plan.md) with checkboxes for each step in the plan. If any step needs my clarification, add a note in the step to get my confirmation. Do not make critical decisions on your own.
Upon completing the plan, ask for my review and approval. After my approval, you can go ahead to execute the plan one step at a time. Once you finish each step, mark the checkboxes as done in the plan.

Focus only on the appointment bookings and doctors searching.

Your Task: Refer to /inception/units/ folder, each md file represents a software unit with the corresponding user stories. Design the Domain Driven Design domain model with all the tactical components including aggregates, entities,
value objects, domain events, policies, repositories, domain services etc. Create a new /construction/ folder in the root directory, write the designs details in a /construction/{unit name}/domain_model.md file.

### Step 2.2:

Your Role: You are an expert software architect and are tasked with creating a logical design of a highly scalable, event-driven system according to a Domain Driven Design domain model. Refer to the Task section for more details.

Plan for the work ahead and write your steps in an md file (plan.md) with checkboxes for each step in the plan. If any step needs my clarification, add a note in the step to get my confirmation. Do not make critical decisions on your own.
Upon completing the plan, ask for my review and approval. After my approval, you can go ahead to execute the same plan one step at a time. Once you finish each step, mark the checkboxes as done in the plan.

Focus only on the appointment bookings and doctors searching.

Your Task: Refer to /construction/{unit name}/domain_model.md file for the domain model. Generate a logical design plan for software source code implementation. Write the plan to the /construction/{unit name}/logical_design.md file.

### Step 2.3:

Your Role: You are an expert software engineer and are tasked with implementing a highly scalable, event-driven system according to the Domain Driven Design logical design. Refer to the Task section for more details.

Plan for the work ahead and write your steps in an md file (plan.md) with checkboxes for each step in the plan. If any step needs my clarification, add a note in the step to get my confirmation. Do not make critical decisions on your own. Upon completing the plan, ask for my review and approval. After my approval, you can go ahead to execute the same plan one step at a time. Once you finish each step, mark the checkboxes as done in the plan.

Focus only on the appointment bookings and doctors searching

Your Task: Refer to /construction/{unit name}/logical_design.md file for the logical design details. Generate a very simple and intuitive angular 19.2.0 and UI based on the mockui/** folder implementation for the bounded context,
for backend we will use Spring Boot 3.x with Java 21 and h2 as a in memory database, using maven as a dependency management, also using Spring Security with AWS Cognito integration
Assume the repositories and the event stores are in-memory. Generate the classes in respective individual files but keep them in the /construction/{unit name}/src/ directory based on the proposed file structure.
Create a simple demo script that can be run locally to verify the implementation.

### Step 2.4:

Your Role: You are an expert software engineer and are tasked with debugging issues with the demo application.

Resolve the issue below and any other issues to ensure that the demo script can be executed successfully.

```
