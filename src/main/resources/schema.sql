create table if not exists `core_UserConnection` (userId varchar(255) not null,
	providerId varchar(255) not null,
	providerUserId varchar(255),
	rank int not null,
	displayName varchar(255),
	profileUrl varchar(512),
	imageUrl varchar(512),
	accessToken varchar(512) not null,
	secret varchar(512),
	refreshToken varchar(512),
	expireTime bigint,
	primary key (userId, providerId, providerUserId));
alter table `core_UserConnection` drop index `UserConnectionRank`;
create unique index `UserConnectionRank` on core_UserConnection(userId, providerId, rank);