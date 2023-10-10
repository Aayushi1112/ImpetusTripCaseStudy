use casestudy;
show tables;
select * from tour_package_and_status;
select * from user_credential;

use casestudyhotel;
show tables;
select * from trip_catalog;
select * from tour_package_and_status;
select * from hotel_info;
delete from hotel_info where(hotel_id='1');
insert into hotel_info (hotel_id,no_of_beds_available_in_hotel,hotel_name,hotelper_bed_price)
values('1','1000','TAJ','344');
select * from hotel_info;



use casestudycar;
show tables;
select * from car_info;
delete from car_info where(car_id='1');
insert into car_info (car_id,car_name,no_of_seats_available_in_car,per_seatprice)
values('1','Mercedes','200','200');

use casestudyflight;
show tables;
select * from flight_info;
delete from flight_info where(flight_id='1');
insert into flight_info (flight_id,no_of_seats_available_in_flight,flight_name,per_seatprice)
values('1','1000','Air_india','344');

use casestudysecurity;
show tables;
select * from users;

INSERT INTO user(username, pass ,enabled) values('user','pass',true);





