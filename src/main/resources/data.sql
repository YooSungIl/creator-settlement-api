INSERT INTO creator (creator_id, creator_nm, platform_commission) VALUES ('creator-1', '김강사', 20);
INSERT INTO creator (creator_id, creator_nm, platform_commission) VALUES ('creator-2', '이강사', 20);
INSERT INTO creator (creator_id, creator_nm, platform_commission) VALUES ('creator-3', '박강사', 20);

INSERT INTO course (course_id, creator_id, course_title, course_price) VALUES ('course-1', 'creator-1', 'Spring Boot 입문', 50000);
INSERT INTO course (course_id, creator_id, course_title, course_price) VALUES ('course-2', 'creator-1', 'JPA 실전', 80000);
INSERT INTO course (course_id, creator_id, course_title, course_price) VALUES ('course-3', 'creator-2', 'Kotlin 기초', 60000);
INSERT INTO course (course_id, creator_id, course_title, course_price) VALUES ('course-4', 'creator-3', 'MSA 설계', 120000);

INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-1', 'course-1', 'student-1', 50000, 'PAID', '2025-03-05 10:00:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-2', 'course-1', 'student-2', 50000, 'PAID', '2025-03-15 14:30:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-3', 'course-2', 'student-3', 80000, 'PAID', '2025-03-20 09:00:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-4', 'course-2', 'student-4', 80000, 'PAID', '2025-03-22 11:00:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-5', 'course-3', 'student-5', 60000, 'PAID', '2025-01-31 23:30:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-6', 'course-3', 'student-6', 60000, 'PAID', '2025-03-10 16:00:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-7', 'course-4', 'student-7', 120000, 'PAID', '2025-02-14 10:00:00');

INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-3', 'course-2', 'student-3', 80000, 'CANCELED', '2025-03-25 10:00:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-4', 'course-2', 'student-4', 30000, 'CANCELED', '2025-03-27 15:00:00');
INSERT INTO sale_record (sale_num, course_id, student_id, amount, sale_status, occurred_at) VALUES ('sale-5', 'course-3', 'student-5', 60000, 'CANCELED', '2025-02-01 00:10:00');