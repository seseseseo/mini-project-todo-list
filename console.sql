create table todo (
                      id int auto_increment primary key ,
                      title varchar(255) not null ,
                      description text,
                      password varchar(255) not null,
                      dueDate date,
                      completed boolean default false,
                      createAt timestamp default current_timestamp,
                      updateAt timestamp default current_timestamp on update current_timestamp
);
alter table todo
    add column author_id int;

alter table todo
    add constraint fk_author_id
        foreign key (author_id) references author(author_id);

INSERT INTO todo (title, description, password, dueDate, completed, createdAt, updatedAt, author_id)
VALUES
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '1372', '2025-04-05', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 293),
    ('UI/UX 개선', 'UI/UX 개선 관련 작업을 진행합니다.', '5992', '2025-04-19', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 294),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '3144', '2025-04-18', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 295),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '8955', '2025-04-21', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 296),
    ('UI/UX 개선', 'UI/UX 개선 관련 작업을 진행합니다.', '6403', '2025-03-30', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 297),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '3346', '2025-04-13', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 298),
    ('성능 최적화', '성능 최적화 관련 작업을 진행합니다.', '4474', '2025-04-09', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 299),
    ('새로운 기능 추가', '새로운 기능 추가 관련 작업을 진행합니다.', '9542', '2025-04-11', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 300),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '3218', '2025-03-31', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 301),
    ('새로운 기능 추가', '새로운 기능 추가 관련 작업을 진행합니다.', '4083', '2025-03-26', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 302),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '6006', '2025-04-05', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 303),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '6990', '2025-04-23', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 304),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '8760', '2025-04-16', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 305),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '1632', '2025-04-23', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 343),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '6337', '2025-03-26', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 344),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '6749', '2025-04-16', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 345),
    ('UI/UX 개선', 'UI/UX 개선 관련 작업을 진행합니다.', '7803', '2025-04-04', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 346),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '6827', '2025-04-19', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 347),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '1372', '2025-04-05', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 348),
    ('UI/UX 개선', 'UI/UX 개선 관련 작업을 진행합니다.', '5992', '2025-04-19', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 349),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '3144', '2025-04-18', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 350),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '8955', '2025-04-21', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 351),
    ('UI/UX 개선', 'UI/UX 개선 관련 작업을 진행합니다.', '6403', '2025-03-30', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 352),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '3346', '2025-04-13', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 353),
    ('성능 최적화', '성능 최적화 관련 작업을 진행합니다.', '4474', '2025-04-09', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 354),
    ('새로운 기능 추가', '새로운 기능 추가 관련 작업을 진행합니다.', '9542', '2025-04-11', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 356),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '3218', '2025-03-31', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 355),
    ('새로운 기능 추가', '새로운 기능 추가 관련 작업을 진행합니다.', '4083', '2025-03-26', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 357),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '6006', '2025-04-05', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 358),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '6990', '2025-04-23', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 359),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '8760', '2025-04-16', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 360),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '1632', '2025-04-23', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 361),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '6337', '2025-03-26', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 362),
    ('기능 개선', '기능 개선 관련 작업을 진행합니다.', '6749', '2025-04-16', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 363),
    ('UI/UX 개선', 'UI/UX 개선 관련 작업을 진행합니다.', '7803', '2025-04-04', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 364),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '6827', '2025-04-19', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 365),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '3542', '2025-04-04', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 366),
    ('버그 수정', '버그 수정 관련 작업을 진행합니다.', '3542', '2025-04-04', 1, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 367),
    ('새로운 기능 추가', '새로운 기능 추가 관련 작업을 진행합니다.', '9669', '2025-04-21', 0, '2025-03-25 16:23:03', '2025-03-25 16:23:03', 368);


drop table author;
create table author(
                       author_id int auto_increment primary key,
                       author    varchar(255) not null,
                       email     varchar(255) unique not null,
                       createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
    INSERT INTO author (authorName, email)
VALUES
    ('연어1', '2s1dsdsono@example.com'),
    ('새우2', 'sadsasdo@example.com'),
    ('강아지3', 'ga23gaji@example.com'),
    ('오리4', 'oi@4124x2a2dmple.com'),
    ('연어1', 'yea223d23ono@example.com'),
    ('새우2', 'saeaa23do@123example.com'),
    ('강아지3', 'da122ngaji@e123xample.com'),
    ('오리4', 'o3xa@d2ple.c123om'),
    ('연어1', 'yeaad4no@examp312le.com'),
    ('새우2', 'sdsaa35sdo@exa132mple.com'),
    ('강아지3', 'gads5ngaji@e132xample.com'),
    ('오리4', 'or1i@ead5mp2le.com'),
    ('연어1', 'yedsadoo@e2x4ample.com'),
    ('새우2', 'sa1odsaasdo@e24xaple.com'),
    ('강아지3', 'gad1dasngaj3i@eample.com'),
    ('오리4', 'ordi@edm3ple.22om'),
    ('연어1', 'yeasdsadono3@exaple.com'),
    ('새우2', 'saewsa2as3do@examle.com'),
    ('강아지3', 'gasd1a2s3ngaji@eample.com'),
    ('오리4', 'ord@exad1m2p3le.cm'),
    ('연어1', 'yeads2ado1no@3exampe.com'),
    ('새우2', 'saeods2as1do@3example.com'),
    ('강아지3', 'gdasd12asngaj3i@e1xample.com'),
    ('오리4', 'ordi@exad2mple.c3om'),
    ('연어1', 'yeaadon1o@exam3le.co1m'),
    ('새우2', 'saewsaas212do@e3xampl1e.com'),
    ('강아지3', 'gasdasng1aj3i@eam1ple.com'),
    ('오리4', 'ordi@exadp3111le.o1m'),
    ('연어1', 'yeasdsan2o@31examp1le.com'),
    ('새우2', 'saewoasd21o@31exa1mple.com'),
    ('강아지3', 'gasdang21aj31i1@example.com'),
    ('오리4', 'or@exd2mple1.c31om'),
    ('연어1', 'asdsadon2o@e1xample.com'),
    ('새우2', 'aewodsaasd2o@31xample.com'),
    ('강아지3', 'gasdasng2aji@example.com'),
    ('오리4', 'or@exadmple2.co1m'),
    ('뚱냥이5', 'dddsda3ugny2ang@1example.com'),
    ('연어6', 'yeoasdno6@example.com'),
    ('새우7', 'saefawo7@example.com'),
    ('강아지8', 'ganasdgaji8@example.com'),
    ('오리9', 'oridsa9@example.com'),
    ('뚱냥이10', 'ddudddngnyang10@example.com'),
    ('두더디', 'yedsaadono@example.com'),
    ('가나디', 'safdasfewdoo@example.com'),
    ('돼지', 'gadddasddngaji@example.com');

select count(author_id) from author;
select count(id) from todo;

select * from todo  where 1=1 order by  updatedAt desc limit : size offset 10;

SELECT t.id, t.title, t.createdAt, t.updatedAt, t.completed, a.authorName, t.dueDate
FROM todo t
         JOIN author a ON t.author_id = a.author_id
ORDER BY t.dueDate DESC
LIMIT 10 OFFSET 0;
select count(id) from todo;