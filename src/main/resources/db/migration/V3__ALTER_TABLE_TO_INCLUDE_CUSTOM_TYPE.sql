alter table unemployed drop column if exists employmentstatus;
alter table unemployed add if not exists employmentStatus employmentstatus default 'UNEMPLOYED';