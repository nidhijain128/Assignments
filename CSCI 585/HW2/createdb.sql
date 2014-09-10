create table buildings
(
buildingId varchar(5),
buildingName varchar(100),
noOfVertices number,
bLoc sdo_geometry,
constraint buildings_pk primary key(buildingId)
);
create table students
(
personId varchar(5),
sLoc sdo_geometry,
constraint students_pk primary key(personId)
);
create table announcementSystems
(
asId varchar(10),
asLoc sdo_geometry,
asRadius number,
asArea sdo_geometry,
constraint announcementSystems_pk primary key(asId)
);
INSERT INTO user_sdo_geom_metadata VALUES ('buildings','bLoc',
  SDO_DIM_ARRAY(
    SDO_DIM_ELEMENT('X', 0, 20, 0.005),
    SDO_DIM_ELEMENT('Y', 0, 20, 0.005)
     ),
  NULL
);
INSERT INTO user_sdo_geom_metadata VALUES ('students','sLoc',
  SDO_DIM_ARRAY(
    SDO_DIM_ELEMENT('X', 0, 20, 0.005),
    SDO_DIM_ELEMENT('Y', 0, 20, 0.005)
     ),
  NULL
);
INSERT INTO user_sdo_geom_metadata VALUES ('announcementSystems','asLoc',
  SDO_DIM_ARRAY(
    SDO_DIM_ELEMENT('X', 0, 20, 0.005),
    SDO_DIM_ELEMENT('Y', 0, 20, 0.005)
     ),
  NULL
);
INSERT INTO user_sdo_geom_metadata VALUES ('announcementSystems','asArea',
  SDO_DIM_ARRAY(
    SDO_DIM_ELEMENT('X', 0, 20, 0.005),
    SDO_DIM_ELEMENT('Y', 0, 20, 0.005)
     ),
  NULL
);
CREATE INDEX buildings_si
   ON buildings(bLoc)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;
CREATE INDEX students_si
   ON students(sLoc)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;
CREATE INDEX announcementSystems_si
   ON announcementSystems(asLoc)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;
CREATE INDEX announcementSystems_si1
   ON announcementSystems(asArea)
   INDEXTYPE IS MDSYS.SPATIAL_INDEX;