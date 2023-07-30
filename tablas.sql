
drop table Pizza;
create table if not exists Pizza(
    id varchar(6) not null ,
    nombre varchar(255) not null ,
    precio decimal(8,2) default 9.99,
    tamaño varchar(255) default 'Grande',
    descripcion text,
    primary key (id)
);

drop table if exists cliente;
create table if not exists cliente(
    id int(8) auto_increment not null,
    nombre varchar(255) not null,
    apellidos varchar(255) not null,
    email varchar(255) not null,
    contraseña blob not null,
    primary key (id),
    unique u_email(email)
);
select id, nombre, apellidos from cliente where email = ?;
insert into cliente (nombre, apellidos, email, contraseña) VALUES ('Marco', 'De la Rosa', '123@gmail.com', aes_encrypt('12345678','cliente_pz2026'));
UPDATE cliente SET contraseña = aes_encrypt(?,?) WHERE id = ?;
select * from cliente;

drop table if exists pedido;
create table if not exists pedido(
    id int auto_increment not null,
    id_cliente int(8) not null,
    fecha timestamp not null,
    total decimal (10,2) not null,
    primary key (id),
    constraint id_cliente foreign key (id_cliente)   references cliente (id)
);
insert into pedido (id_cliente, fecha, total) VALUES (?,CONVERT_TZ(NOW(), '+00:00', '-05:00'),?);
select * from pedido;

drop table if exists detalle_pedidos;
create table if not exists detalle_pedidos (
    id int (10) not null auto_increment,
    nombre varchar(255) not null,
    precio decimal (8,2) not null,
    tamanio varchar(255) not null,
    cantidad int (5) not null,
    id_pedido int not null,
    primary key (id),
    constraint id_pedido_fk FOREIGN KEY (id_pedido) references pedido(id)
);
delete from detalle_pedidos;
delete from pedido;
delete from pedido where id = ?;

select * from pedido;
select * from detalle_pedidos;